package com.example.inventori.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventori.R;
import com.example.inventori.model.StocksModel;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InventRestock extends AppCompatActivity {

    Spinner spinRestock;
    ArrayList<String> restockList = new ArrayList<>();
    ArrayAdapter<String> restockAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_restock);

        requestQueue = Volley.newRequestQueue(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinRestock = findViewById(R.id.spinRestock);

        restockList = new ArrayList<>();

        String URL = "http://10.0.2.2/notif/restock.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("stocks");
                    for (int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String bahan = jsonObject.optString("bahan_baku");
                        restockList.add(bahan);
                        restockAdapter = new ArrayAdapter<>(InventRestock.this, android.R.layout.simple_spinner_item, restockList);
                        restockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinRestock.setAdapter(restockAdapter);
                    }
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
    }


}