package com.example.inventori.Activity.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.APIRequestMenu;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMenu extends AppCompatActivity {
    EditText etMenuName, etMenuPrice, etMenuDesc;
    Button btnSaveMenu;
    String menu,price,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah menu");

        etMenuDesc = findViewById(R.id.etMenuDesc);
        etMenuName = findViewById(R.id.etMenuName);
        etMenuPrice = findViewById(R.id.etMenuPrice);
        btnSaveMenu = findViewById(R.id.btnSaveMenu);

        btnSaveMenu.setOnClickListener(view -> {
            menu = etMenuName.getText().toString().trim();
            price = etMenuPrice.getText().toString().trim();
            desc = etMenuDesc.getText().toString().trim();

            if (menu.isEmpty()){
                etMenuName.setError("harus diisi");
            }
            else if (price.isEmpty()){
                etMenuPrice.setError("harus diisi");
            }
            else {
               addMenu();
            }
        });
    }

    private void addMenu(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> addData = dataMenu.createMenu(menu, Integer.parseInt(price), desc);

        addData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getPesan();

                Toast.makeText(AddMenu.this, "berhasil menyimpan menu" +"(" +code+ ")" +" "+ pesan,
                        Toast.LENGTH_SHORT).show();
                addTable();
                setResult(RESULT_OK);

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AddMenu.this, "gagal simpan menu: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTable(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> createTable = dataKomposisi.createTable(
                menu.trim().replaceAll("\\s","_"));

        createTable.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddMenu.this);
                builder.setTitle(menu);
                builder.setMessage("Atur komposisi?");
                builder.setNegativeButton("Nanti saja", (dialogInterface, i) -> finish());
                builder.setPositiveButton("Atur Komposisi", (dialogInterface, i) -> {
                    Intent intent = new Intent(AddMenu.this, KomposisiSet.class);
                    intent.putExtra("menu", menu);
                    startActivity(intent);
                    finish();
                });
                builder.show();
                Toast.makeText(AddMenu.this, "berhasil", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                deleteMenu();
                Toast.makeText(AddMenu.this, "gagal: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMenu(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> deleteData = dataMenu.deleteMenuByname(menu);

        deleteData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(AddMenu.this, "Menu gagal dibuat", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AddMenu.this, "gagal menghapus data"+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}