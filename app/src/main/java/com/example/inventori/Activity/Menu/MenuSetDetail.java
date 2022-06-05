package com.example.inventori.Activity.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.APIRequestMenu;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterKomposisi;
import com.example.inventori.R;
import com.example.inventori.model.KomposisiModel;
import com.example.inventori.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuSetDetail extends AppCompatActivity {

    EditText etDetailMenu, etDetailPrice, etDetailDesc,etDetailID, etBahany, etJumlahy, etSatuany;
    Button btnDetailSave, btnTambahKomposisiy;
    ListView lvKomposisiy;
    List<KomposisiModel> komposisiModels;
    AdapterKomposisi adapterKomposisi;
    int id,harga, jumlah, komposisiId, i;
    String menu,deskripsi, bahan, satuan, user;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_set_detail);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        etDetailMenu = findViewById(R.id.etDetailMenu);
        etDetailPrice = findViewById(R.id.etDetailPrice);
        etDetailDesc = findViewById(R.id.etDetailDesc);
        etDetailID = findViewById(R.id.etDetailID);
        etBahany = findViewById(R.id.etBahany);
        etJumlahy = findViewById(R.id.etJumlahy);
        etSatuany = findViewById(R.id.etSatuany);
        lvKomposisiy = findViewById(R.id.lvKomposisiy);
        btnTambahKomposisiy = findViewById(R.id.btnTambahKomposisiy);
        btnDetailSave = findViewById(R.id.btnDetailSave);

        Intent intent = getIntent();

        id = intent.getIntExtra("menuId",-1);
        harga = intent.getIntExtra("menuPrice", -1);
        menu = intent.getStringExtra("menuName");
        deskripsi = intent.getStringExtra("menuDesc");

        if(AdapterKomposisi.updateChange){
            getKomposisi();
        }
        getKomposisi();

        etDetailID.setText(id+"");
        etDetailMenu.setText(menu);
        etDetailPrice.setText(harga+"");
        etDetailDesc.setText(deskripsi);

        btnDetailSave.setOnClickListener(view -> {
            id = Integer.parseInt(etDetailID.getText().toString().trim());
            harga = Integer.parseInt(etDetailPrice.getText().toString().trim());
            menu = etDetailMenu.getText().toString().trim();
            deskripsi = etDetailDesc.getText().toString().trim();
            updateData();

            for(i = 0; i<komposisiModels.size();i++) {
                View view1 = lvKomposisiy.getChildAt(i);
                EditText etIdBahan = view1.findViewById(R.id.tvIdBahan);
                EditText etBahan = view1.findViewById(R.id.tvBahan);
                EditText etJumlah = view1.findViewById(R.id.tvJumlah);
                EditText etSatuan = view1.findViewById(R.id.tvSatuan);

                komposisiId = Integer.parseInt(etIdBahan.getText().toString().trim());
                bahan = etBahan.getText().toString().trim();
                jumlah = Integer.parseInt(etJumlah.getText().toString().trim());
                satuan = etSatuan.getText().toString().trim();

                //updateKomposisi();
            }
            if(!etBahany.getText().toString().trim().isEmpty() &&
                    !etJumlahy.getText().toString().trim().isEmpty() &&
                    !etSatuany.getText().toString().trim().isEmpty()){
                bahan = etBahany.getText().toString().trim();
                String jumlahBahan = etJumlahy.getText().toString().trim();
                jumlah = Integer.parseInt(jumlahBahan);
                satuan = etSatuany.getText().toString().trim();
                addKomposisi();
            }
        });

        btnTambahKomposisiy.setOnClickListener(view -> {
            etBahany.setVisibility(View.VISIBLE);
            etJumlahy.setVisibility(View.VISIBLE);
            etSatuany.setVisibility(View.VISIBLE);

            if(etBahany.getText().toString().isEmpty()){
                etBahany.setError("harus diisi");
            }
            else if(etJumlahy.getText().toString().isEmpty()){
                etJumlahy.setError("harus diisi");
            }
            else if (etSatuany.getText().toString().isEmpty()){
                etSatuany.setError("harus diisi");
            }
            else {
                bahan = etBahany.getText().toString().trim();
                jumlah = Integer.parseInt(etJumlahy.getText().toString().trim());
                satuan = etSatuany.getText().toString().trim();
                addKomposisi();
            }
        });

    }

    public void updateData(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> update = dataMenu.updateMenu(id,menu,harga,deskripsi);

        update.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                finish();
                Toast.makeText(MenuSetDetail.this, "Berhasil menyimpan",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "Gagal: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> komposisi = dataKomposisi.getKomposisi(
                menu, user);
        komposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                komposisiModels = response.body().getKomposisiModelList();
                if(komposisiModels != null) {
                    adapterKomposisi = new AdapterKomposisi(MenuSetDetail.this,komposisiModels);
                    lvKomposisiy.setAdapter(adapterKomposisi);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void addKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> tambahKompsisi = dataKomposisi.addKomposisi(
                user,bahan, jumlah, satuan, menu);
        tambahKompsisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                etBahany.setText(null);
                etJumlahy.setText(null);
                etSatuany.setText(null);
                etBahany.setVisibility(View.GONE);
                etJumlahy.setVisibility(View.GONE);
                etSatuany.setVisibility(View.GONE);
                getKomposisi();
                Toast.makeText(MenuSetDetail.this, "Komposisi ditambahkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(MenuSetDetail.this, "Komposisi gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void updateKomposisi(){
//        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
//        Call<ResponseModel> ubahKomposisi = dataKomposisi.updateKomposisi(
//                komposisiId, bahan, jumlah, satuan);
//        ubahKomposisi.enqueue(new Callback<ResponseModel>() {
//            @Override
//            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                Toast.makeText(MenuSetDetail.this, "Berhasil merubah", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Toast.makeText(MenuSetDetail.this, "Gagal merubah: "+t.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//                System.out.println(t.getMessage());
//            }
//        });
//    }
}