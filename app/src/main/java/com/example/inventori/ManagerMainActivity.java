package com.example.inventori;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.inventori.BottomNavBar.ManagerChatFragment;
import com.example.inventori.BottomNavBar.ManagerHomeFragment;
import com.example.inventori.BottomNavBar.ManagerSettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.inventori.Activity.User.ManagerLoginActivity;
import com.example.inventori.Activity.User.UserSession;

public class ManagerMainActivity extends AppCompatActivity {
    UserSession userSession;
    String manager;
    FrameLayout flManagerMainLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        userSession = new UserSession(this);
        if(!userSession.isLoggedIn()) moveToLogin();
        else manager=userSession.getManagerDetail().get("manager");

        flManagerMainLayout = findViewById(R.id.flManagerMainLayout);
        bottomNavigationView = findViewById(R.id.navView);

        bottomNavigationView.setItemHorizontalTranslationEnabled(true);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().
                replace(R.id.flManagerMainLayout, new ManagerHomeFragment());
        transaction.commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            fragment = null;
                if(item.getItemId()== R.id.managerHomeNav) fragmentManager.beginTransaction().
                        replace(R.id.flManagerMainLayout, new ManagerHomeFragment()).commit();
                if(item.getItemId()== R.id.managerChatNav) fragmentManager.beginTransaction().
                        replace(R.id.flManagerMainLayout, new ManagerChatFragment()).commit();
                if(item.getItemId()== R.id.managerSettingNav) fragmentManager.beginTransaction().
                        replace(R.id.flManagerMainLayout, new ManagerSettingsFragment()).commit();
            return true;
        });

    }

    private void moveToLogin() {
        Intent intent = new Intent(this, ManagerLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}