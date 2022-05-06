package com.example.inventori.Activity.Stock;

import androidx.appcompat.app.AppCompatActivity;

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

public class AddStock extends AppCompatActivity {

    EditText etNameStock, etJumlah, etSatuan, etStockPesan, etStockWaktuTgu;
    Button btnStockAdd;
    int waktu, min_pesan, jumlah;
    String bahan_baku, satuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        etJumlah = findViewById(R.id.etJumlahStock);
        etNameStock = findViewById(R.id.etNameStock);
        etSatuan = findViewById(R.id.etSatuanStock);
        etStockPesan = findViewById(R.id.etPesanStock);
        etStockWaktuTgu = findViewById(R.id.etStockWaktuTgu);
        btnStockAdd = findViewById(R.id.btnStockAdd);

        btnStockAdd.setOnClickListener(view -> {
            if(etJumlah.getText().toString().isEmpty() || etNameStock.getText().toString().isEmpty() ||
                    etSatuan.getText().toString().isEmpty() || etStockPesan.getText().toString().isEmpty() ||
                    etStockWaktuTgu.getText().toString().isEmpty()){
                Toast.makeText(AddStock.this, "Harus diisi", Toast.LENGTH_SHORT).show();
            }
            else {
                bahan_baku = etNameStock.getText().toString().trim();
                jumlah = Integer.parseInt(etJumlah.getText().toString().trim());
                waktu = Integer.parseInt(etStockWaktuTgu.getText().toString().trim());
                min_pesan = Integer.parseInt(etStockPesan.getText().toString().trim());
                satuan = etSatuan.getText().toString().trim();
                addStock();
            }
        });
    }

    private void addStock(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> addData = stockData.addData(bahan_baku, jumlah, satuan, min_pesan, waktu);

        addData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                setResult(RESULT_OK);
                finish();
                Toast.makeText(AddStock.this, "berhasil", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AddStock.this, "gagal: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}