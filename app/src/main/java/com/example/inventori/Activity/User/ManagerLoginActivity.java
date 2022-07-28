package com.example.inventori.Activity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIAccounts;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.ManagerMainActivity;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerLoginActivity extends AppCompatActivity {
    EditText etManagerName, etManagerPassword;
    Button btnLoginManager;
    TextView tvManagerDaftar, tvUserLogin;
    String manager,password;
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        etManagerName = findViewById(R.id.etManagerName);
        etManagerPassword = findViewById(R.id.etManagerPassword);
        btnLoginManager = findViewById(R.id.btnLoginManager);
        tvManagerDaftar = findViewById(R.id.tvManagerDaftar);
        tvUserLogin = findViewById(R.id.tvUserLogin);

        tvUserLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        tvManagerDaftar.setOnClickListener(view -> {
            startActivity(new Intent(this, ManagerRegisterActivity.class));
            finish();
        });

        btnLoginManager.setOnClickListener(view -> {
            manager = etManagerName.getText().toString().trim();
            password = etManagerPassword.getText().toString().trim();
            if(manager.isEmpty()||password.isEmpty()){
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            }else {
                login();
            }
        });
    }

    private void login() {
        APIAccounts login = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> loginManager = login.loginManager(manager, password);

        loginManager.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if(response.body()!=null) {
                    String pesan = response.body().getPesan();
                    String status = response.body().getStatus();
                    if(status.equals("true") ){
                        session = new UserSession(ManagerLoginActivity.this);
                        session.createManagerSession(manager);
                        startActivity(new Intent(ManagerLoginActivity.this, ManagerMainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(ManagerLoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable throwable) {

            }
        });
    }
}