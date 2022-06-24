package com.example.inventori.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIAccounts;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.MainActivity;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    TextView tvDaftar;
    String user, password, status;
    Button btnLogin;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvDaftar = findViewById(R.id.tvDaftar);

        btnLogin.setOnClickListener(view -> {
            if(etUsername.getText().toString().trim().isEmpty() ||
                    etPassword.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            }
            else {
                user = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                login();
            }
        });

        tvDaftar.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void login() {
        APIAccounts data = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> loginUser = data.login(user, password);

        loginUser.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                String pesan = response.body().getPesan();
                status = response.body().getStatus();
                if(status.equals("true") ){
                    userSession = new UserSession(getApplicationContext());
                    userSession.createSession(user);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Gagal " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}