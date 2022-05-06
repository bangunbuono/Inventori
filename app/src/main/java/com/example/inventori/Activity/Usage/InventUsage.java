package com.example.inventori.Activity.Usage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.inventori.R;

public class InventUsage extends AppCompatActivity {
    Button btnUsageAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_usage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnUsageAuto = findViewById(R.id.btnUsageAuto);

        btnUsageAuto.setOnClickListener(view -> {
            startActivity(new Intent(InventUsage.this, InventUsageAuto.class));
        });
    }
}