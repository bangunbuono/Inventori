package com.example.inventori.Activity.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Adapter.AdapterKomposisi;
import com.example.inventori.R;
import com.example.inventori.model.KomposisiModel;
import com.example.inventori.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomposisiSet extends AppCompatActivity {

    EditText etBahanx, etJumlahx, etSatuanx;
    TextView tvKomposisi;
    Button btnTambahKomposisi, btnSimpanKomposisi;
    ListView lvKomposisi;
    AdapterKomposisi adapterKomposisi;
    public List<KomposisiModel> listKomposisi;
    String bahan,satuan,table;
    int jumlah, i, komposisiId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komposisi_set);

        etBahanx = findViewById(R.id.etBahanx);
        etJumlahx = findViewById(R.id.etJumlahx);
        etSatuanx = findViewById(R.id.etSatuanx);
        lvKomposisi = findViewById(R.id.lvKomposisi);
        tvKomposisi = findViewById(R.id.tvKomposisi);
        btnTambahKomposisi = findViewById(R.id.btnTambahKomposisi);
        btnSimpanKomposisi = findViewById(R.id.btnSimpanKomposisi);

        Intent intent = getIntent();
        table = intent.getStringExtra("menu").trim().replaceAll("\\s", "_");
        tvKomposisi.setText("komposisi " + intent.getStringExtra("menu"));

        btnTambahKomposisi.setOnClickListener(view -> {
            if(etBahanx.getText().toString().isEmpty()){
                etBahanx.setError("harus diisi");
            }
            else if(etJumlahx.getText().toString().isEmpty()){
                etJumlahx.setError("harus diisi");
            }
            else if (etSatuanx.getText().toString().isEmpty()){
                etSatuanx.setError("harus diisi");
            }
            else {
                bahan = etBahanx.getText().toString().trim();
                jumlah = Integer.parseInt(etJumlahx.getText().toString().trim());
                satuan = etSatuanx.getText().toString().trim();
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
            bahan = etBahanx.getText().toString().trim();
            jumlah = Integer.parseInt(etJumlahx.getText().toString().trim());
            satuan = etSatuanx.getText().toString().trim();
            addKomposisi();
            finish();
        });
    }

    private void addKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> tambahKompsisi = dataKomposisi.addKomposisi(
                table,bahan, jumlah, satuan);
        tambahKompsisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                etBahanx.setText(null);
                etJumlahx.setText(null);
                etSatuanx.setText(null);
                getKomposisi();
                Toast.makeText(KomposisiSet.this, "Komposisi ditambahkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(KomposisiSet.this, "Komposisi gagal ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> komposisi = dataKomposisi.getKomposisi(
                table);
        komposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                listKomposisi = response.body().getKomposisiModelList();
                adapterKomposisi = new AdapterKomposisi(KomposisiSet.this,listKomposisi);
                lvKomposisi.setAdapter(adapterKomposisi);
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
                table, komposisiId, bahan, jumlah, satuan);
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
}