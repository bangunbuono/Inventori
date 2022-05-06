package com.example.inventori.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventorySetDetail extends AppCompatActivity {

    EditText etStockName, etStockJumlah, etStockSatuan, etStockID, etStockWaktu, etStockMinPesan;
    Button btnStockSave;
    int id, waktu, min_pesan, jumlah;
    String bahan_baku, satuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_set_detail);

        etStockID = findViewById(R.id.etStockID);
        etStockJumlah = findViewById(R.id.etStockJumlah);
        etStockName = findViewById(R.id.etStockName);
        etStockSatuan = findViewById(R.id.etStockSatuan);
        etStockWaktu = findViewById(R.id.etStockWaktu);
        etStockMinPesan = findViewById(R.id.etStockMinPesan);
        btnStockSave = findViewById(R.id.btnStockSave);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
        waktu = intent.getIntExtra("waktu",-1);
        jumlah = intent.getIntExtra("jumlah",-1);
        min_pesan = intent.getIntExtra("min_pesan",-1);
        bahan_baku = intent.getStringExtra("bahan");
        satuan = intent.getStringExtra("satuan");

        etStockID.setText(id+"");
        etStockWaktu.setText(waktu+"" );
        etStockSatuan.setText(satuan);
        etStockName.setText(bahan_baku);
        etStockMinPesan.setText(min_pesan+"");
        etStockJumlah.setText(jumlah+"");

        btnStockSave.setOnClickListener(view -> {
            id = Integer.parseInt(etStockID.getText().toString().trim());
            bahan_baku = etStockName.getText().toString().trim();
            jumlah = Integer.parseInt(etStockJumlah.getText().toString().trim());
            waktu = Integer.parseInt(etStockWaktu.getText().toString().trim());
            min_pesan = Integer.parseInt(etStockMinPesan.getText().toString().trim());
            satuan = etStockSatuan.getText().toString().trim();

            updateStock();
        });

    }

    private void updateStock(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> updataData = stockData.updateData(id,bahan_baku,jumlah,satuan,min_pesan,waktu);

        updataData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                finish();
                Toast.makeText(InventorySetDetail.this, "berhasil menyimpan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(InventorySetDetail.this, "gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}