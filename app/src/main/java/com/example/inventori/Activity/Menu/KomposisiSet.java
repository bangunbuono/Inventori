package com.example.inventori.Activity.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.APIRestock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterKomposisi;
import com.example.inventori.Adapter.AdapterSpinnerKomposisi;
import com.example.inventori.R;
import com.example.inventori.model.KomposisiModel;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.RestockModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomposisiSet extends AppCompatActivity {

    EditText etJumlahx;
    TextView tvKomposisi, tvSatuanx;
    Spinner spinnerBahanx;
    Button btnTambahKomposisi, btnSimpanKomposisi;
    ListView lvKomposisi;
    AdapterKomposisi adapterKomposisi;
    public List<KomposisiModel> listKomposisi;
    List<RestockModel> listBahan;
    AdapterSpinnerKomposisi adapterSpinnerBahan;
    String bahan,satuan,menu, user;
    int jumlah, i, komposisiId;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komposisi_set);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        spinnerBahanx = findViewById(R.id.spinnerBahanx);
        etJumlahx = findViewById(R.id.etJumlahx);
        tvSatuanx = findViewById(R.id.tvSatuanx);
        lvKomposisi = findViewById(R.id.lvKomposisi);
        tvKomposisi = findViewById(R.id.tvKomposisi);
        btnTambahKomposisi = findViewById(R.id.btnTambahKomposisi);
        btnSimpanKomposisi = findViewById(R.id.btnSimpanKomposisi);

        Intent intent = getIntent();
        menu = intent.getStringExtra("menu");
        tvKomposisi.setText("komposisi " + intent.getStringExtra("menu"));

        listBahan();
        spinnerBahanx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bahan = listBahan.get(i).getBahan_baku();
                satuan = listBahan.get(i).getSatuan();
                Toast.makeText(KomposisiSet.this, bahan + satuan, Toast.LENGTH_SHORT).show();
                tvSatuanx.setText(satuan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnTambahKomposisi.setOnClickListener(view -> {
//            spinnerBahanx.setVisibility(View.VISIBLE);
//            tvSatuanx.setVisibility(View.VISIBLE);
//            etJumlahx.setVisibility(View.VISIBLE);

            if(etJumlahx.getText().toString().isEmpty()){
                etJumlahx.setError("harus diisi");
            }
            else {
                jumlah = Integer.parseInt(etJumlahx.getText().toString().trim());
                addKomposisi();
            }
        });

        btnSimpanKomposisi.setOnClickListener(view -> {
            for(i = 0; i<listKomposisi.size();i++){
                View view1 = lvKomposisi.getChildAt(i);
                EditText etIdBahan = view1.findViewById(R.id.etIdBahan);
                EditText etBahan = view1.findViewById(R.id.etBahan);
                EditText etJumlah = view1.findViewById(R.id.etJumlah);
                EditText etSatuan = view1.findViewById(R.id.etSatuan);

                komposisiId = Integer.parseInt(etIdBahan.getText().toString().trim());
                bahan = etBahan.getText().toString().trim();
                jumlah = Integer.parseInt(etJumlah.getText().toString().trim());
                satuan = etSatuan.getText().toString().trim();

                updateKomposisi();
            }
            if(!etJumlahx.getText().toString().trim().isEmpty()){
                spinnerBahanx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        bahan = listBahan.get(i).getBahan_baku();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                jumlah = Integer.parseInt(etJumlahx.getText().toString().trim());
                addKomposisi();
            }
            finish();
        });
    }

    private void addKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> tambahKompsisi = dataKomposisi.addKomposisi(user, bahan, jumlah, satuan, menu);
        tambahKompsisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body()!=null;
                int code = response.body().getCode();
                if(code == 2){
                    Toast.makeText(KomposisiSet.this, "Gagal: komposisi sudah ada", Toast.LENGTH_SHORT).show();
                }else if(code == 0){
                    Toast.makeText(KomposisiSet.this, "Gagal menambahkan komposisi", Toast.LENGTH_SHORT).show();
                }else {
                    etJumlahx.setText(null);
//                tvSatuanx.setVisibility(View.GONE);
//                etJumlahx.setVisibility(View.GONE);
//                spinnerBahanx.setVisibility(View.GONE);
                    getKomposisi();
                    Toast.makeText(KomposisiSet.this, "Komposisi ditambahkan",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, Throwable t) {
                Toast.makeText(KomposisiSet.this, "Komposisi gagal ditambahkan",
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
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                assert response.body() != null;
                listKomposisi = response.body().getKomposisiModelList();
                if (listKomposisi != null){
                    adapterKomposisi = new AdapterKomposisi(KomposisiSet.this,listKomposisi);
                    lvKomposisi.setAdapter(adapterKomposisi);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(KomposisiSet.this, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void updateKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> ubahKomposisi = dataKomposisi.updateKomposisi(
                komposisiId, bahan, jumlah, satuan);
        ubahKomposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(KomposisiSet.this, "Berhasil merubah", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(KomposisiSet.this, "Gagal merubah: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void listBahan(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                assert response.body() != null;
                listBahan = response.body().getStocks();
                if(listBahan != null){
                    adapterSpinnerBahan = new AdapterSpinnerKomposisi(KomposisiSet.this,listBahan);
                    spinnerBahanx.setAdapter(adapterSpinnerBahan);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }
}