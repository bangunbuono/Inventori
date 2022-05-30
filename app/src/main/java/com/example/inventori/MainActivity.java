package com.example.inventori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventori.Activity.InventForecast;
import com.example.inventori.Activity.InventReport;
import com.example.inventori.Activity.Restock.InventRestock;
import com.example.inventori.Activity.Usage.InventUsage;
import com.example.inventori.Activity.Stock.InventorySet;
import com.example.inventori.Activity.Menu.MenuSet;
import com.example.inventori.Activity.User.LoginActivity;
import com.example.inventori.Activity.User.UserSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    TextView tvUser, tvLogout;
    BottomNavigationView navView;
    FragmentManager fragmentManager;
    Fragment fragment;
    Button btnInvSet, btnUsage, btnForecast, btnMenuSet, btnRestock, btnReport;
    String user;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSession = new UserSession(getApplicationContext());
        if(!userSession.isLoggedIn()){
            moveToLogin();
        }

        user = userSession.getUserDetail().get("username");

        tvUser = findViewById(R.id.tvUser);
        navView = findViewById(R.id.navView);
        btnInvSet = findViewById(R.id.btnInvSet);
        btnUsage = findViewById(R.id.btnUsage);
        btnForecast = findViewById(R.id.btnForecast);
        btnMenuSet = findViewById(R.id.btnMenuSet);
        btnRestock = findViewById(R.id.btnRestock);
        btnReport = findViewById(R.id.btnReport);
        tvLogout = findViewById(R.id.tvLogOut);

        tvLogout.setOnClickListener(view1 -> {
            userSession = new UserSession(getApplicationContext());
            userSession.logOutSession();
            moveToLogin();
        });

        tvUser.setText(user.replace(user.charAt(0), user.toUpperCase().charAt(0))+"'s Home");
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

        btnInvSet.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this, InventorySet.class);
            startActivity(i);
        });

        btnMenuSet.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this, MenuSet.class);
            startActivity(i);
        });

        btnUsage.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this, InventUsage.class);
            startActivity(i);
        });

        btnRestock.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this, InventRestock.class);
            startActivity(i);
        });

        btnForecast.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this, InventForecast.class);
            startActivity(i);
        });

        btnReport.setOnClickListener(view ->{
            Intent i = new Intent(MainActivity.this, InventReport.class);
            startActivity(i);
        });


    }

    private void moveToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}