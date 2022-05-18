package com.example.inventori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.inventori.Activity.InventForecast;
import com.example.inventori.Activity.InventReport;
import com.example.inventori.Activity.Restock.InventRestock;
import com.example.inventori.Activity.Usage.InventUsage;
import com.example.inventori.Activity.Stock.InventorySet;
import com.example.inventori.Activity.Menu.MenuSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    FragmentManager fragmentManager;
    Fragment fragment;
    Button btnInvSet, btnUsage, btnForecast, btnMenuSet, btnRestock, btnReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.navView);
        btnInvSet = findViewById(R.id.btnInvSet);
        btnUsage = findViewById(R.id.btnUsage);
        btnForecast = findViewById(R.id.btnForecast);
        btnMenuSet = findViewById(R.id.btnMenuSet);
        btnRestock = findViewById(R.id.btnRestock);
        btnReport = findViewById(R.id.btnReport);

        navView.setItemHorizontalTranslationEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.homeFragment))).
                hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.settingFragment))).
                hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.chatFragment))).commit();


        navView.setOnItemSelectedListener(item -> {
            fragment = null;
            switch (item.getItemId()) {
                case R.id.homeNav:
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.homeFragment))).
                            hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.settingFragment))).
                            hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.chatFragment))).commit();
                    break;
                case R.id.chatNav:
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.homeFragment))).
                            hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.settingFragment))).
                            show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.chatFragment))).commit();
                    break;
                case R.id.settingNav:
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.homeFragment))).
                            show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.settingFragment))).
                            hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.chatFragment))).commit();
                    break;
            }
            return true;
        });

        btnInvSet.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, InventorySet.class)));

        btnMenuSet.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, MenuSet.class)));

        btnUsage.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, InventUsage.class)));

        btnRestock.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, InventRestock.class)));

        btnForecast.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, InventForecast.class)));

        btnReport.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, InventReport.class)));


    }
}