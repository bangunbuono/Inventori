package com.example.inventori.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.inventori.R;

public class InventRestock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_restock);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}