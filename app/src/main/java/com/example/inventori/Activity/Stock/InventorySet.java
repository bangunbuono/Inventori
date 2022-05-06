package com.example.inventori.Activity.Stock;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Adapter.AdapterStocks;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.StocksModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventorySet extends AppCompatActivity{

    ListView lvStocks;
    AdapterStocks adapterStocks;
    List<StocksModel> stocksModelList;
    TextView tvAddStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_set);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode()==RESULT_OK){
                        getStocks();
                    }
                });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvStocks = findViewById(R.id.lvStocks);
        tvAddStock = findViewById(R.id.tvAddStock);
        stocksModelList = new ArrayList<>();
        getStocks();

        tvAddStock.setOnClickListener(view -> {
            launcher.launch(new Intent(InventorySet.this, AddStock.class));
        });

    }
    private void getStocks(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> showData = stockData.showStock();

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getPesan();
                stocksModelList = response.body().getStocksModels();
                adapterStocks = new AdapterStocks(InventorySet.this, stocksModelList);
                lvStocks.setAdapter(adapterStocks);
                Toast.makeText(InventorySet.this, "berhasil "+code+" "+pesan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(InventorySet.this, "Gagal memuat stock: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.refresh){
            getStocks();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStocks();
    }
}
