package com.example.inventori.Activity.Restock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.APIRestock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterSpinnerRestock;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.RestockModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventRestock extends AppCompatActivity {

    Spinner spinRestock;
    ArrayList<RestockModel> restockList = new ArrayList<>();
    TextView tvRestockSatuan;
    EditText etRestockJumlah;
    AdapterSpinnerRestock adapterRestock;
    Button btnCollectRestock;
    String user;
    UserSession userSession;
    int id, jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_restock);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinRestock = findViewById(R.id.spinRestock);
        tvRestockSatuan = findViewById(R.id.tvRestockSatuan);
        btnCollectRestock = findViewById(R.id.btnCollect);
        etRestockJumlah = findViewById(R.id.etRestockJumlah);

        restockList = new ArrayList<>();

        restockList();
        spinRestock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvRestockSatuan.setText(restockList.get(i).getSatuan());
                id = restockList.get(i).getId();
                Toast.makeText(InventRestock.this, "item: "+restockList.get(i).getBahan_baku(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnCollectRestock.setOnClickListener(view1 -> {
            jumlah = Integer.parseInt(etRestockJumlah.getText().toString().trim());
            restockAdd();
        });

    }

    private void restockList(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                restockList = response.body().getStocks();
                adapterRestock = new AdapterSpinnerRestock(InventRestock.this, restockList);
                spinRestock.setAdapter(adapterRestock);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    private void restockAdd(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> addStock = stockData.addStocks(id,jumlah);

        addStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                etRestockJumlah.setText(null);
                Toast.makeText(InventRestock.this, "berhasil menambahkan stok", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(InventRestock.this, "gaga menambahkan stok "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}