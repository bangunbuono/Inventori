package com.example.inventori.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Adapter.AdapterSpinnerRestock;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.RestockModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class InventRestock extends AppCompatActivity {

    Spinner spinRestock;
    ArrayList<RestockModel> restockList = new ArrayList<>();
    RequestQueue requestQueue;
    TextView tvRestockSatuan;
    EditText etRestockJumlah;
    AdapterSpinnerRestock adapterRestock;
    Button btnCollectRestock;
    int id, jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_restock);

        requestQueue = Volley.newRequestQueue(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinRestock = findViewById(R.id.spinRestock);
        tvRestockSatuan = findViewById(R.id.tvRestockSatuan);
        btnCollectRestock = findViewById(R.id.btnCollect);
        etRestockJumlah = findViewById(R.id.etRestockJumlah);

        restockList = new ArrayList<>();

        String URL = "http://10.0.2.2/notif/restocktest.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("stocks");
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String bahan = jsonObject.optString("bahan_baku");
                        String satuan = jsonObject.optString("satuan");
                        id = jsonObject.optInt("id");
                        restockList.add(new RestockModel(id, bahan, satuan));
                    }
                    adapterRestock = new AdapterSpinnerRestock(InventRestock.this, restockList);
                    spinRestock.setAdapter(adapterRestock);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
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