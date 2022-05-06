package com.example.inventori.Activity.Menu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestMenu;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Adapter.AdapterMenuSet;
import com.example.inventori.R;
import com.example.inventori.model.MenuModel;
import com.example.inventori.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuSet extends AppCompatActivity {

    AdapterMenuSet adapter;
    ListView lvMenu;
    List<MenuModel> listMenu;
    TextView tvAddMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_set);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== RESULT_OK){
                        retrieveData();
                    }
                });

        tvAddMenu = findViewById(R.id.tvAddMenu);
        lvMenu = findViewById(R.id.lvMenu);
        listMenu = new ArrayList<>();
        retrieveData();

        tvAddMenu.setOnClickListener(view ->
                launcher.launch(new Intent(this, AddMenu.class)));

    }

    public void retrieveData(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> showData = dataMenu.showMenu();

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getPesan();

                listMenu = response.body().getData();
                adapter = new AdapterMenuSet(MenuSet.this, listMenu);
                lvMenu.setAdapter(adapter);
                Toast.makeText(MenuSet.this,
                        "success: " +"(" +code+ ")" +" "+ pesan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MenuSet.this, "error:" +t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
}