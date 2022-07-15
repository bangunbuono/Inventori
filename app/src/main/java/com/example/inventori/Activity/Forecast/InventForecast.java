package com.example.inventori.Activity.Forecast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inventori.R;

public class InventForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_forecast);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}