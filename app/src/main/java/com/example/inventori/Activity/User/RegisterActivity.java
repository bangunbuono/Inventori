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
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etRegisUser, etRegisPassword, etConfirmPassword;
    Button btnRegis;
    TextView tvMasuk;
    String user, password, confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisUser = findViewById(R.id.etRegisUser);
        etRegisPassword = findViewById(R.id.etRegisPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegis = findViewById(R.id.btnRegis);
        tvMasuk = findViewById(R.id.tvMasuk);

        btnRegis.setOnClickListener(view -> {
            user = etRegisUser.getText().toString().trim();
            password = etRegisPassword.getText().toString().trim();
            confirmPass = etConfirmPassword.getText().toString().trim();

            if(user.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            }else if (!password.equals(confirmPass)){
                Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
            }else {
                register();
            }
        });

        tvMasuk.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void register(){
        APIAccounts userRegister = ServerConnection.connection().create(APIAccounts.class);
        Call<ResponseModel> register = userRegister.register(user, password);

        register.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getPesan();
                if (code == 2){
                    Toast.makeText(RegisterActivity.this, "Gagal: "+pesan, Toast.LENGTH_SHORT).show();
                }else if(code == 0){
                    Toast.makeText(RegisterActivity.this, "Gagal input data", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegisterActivity.this, "Berhasil daftar", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

}