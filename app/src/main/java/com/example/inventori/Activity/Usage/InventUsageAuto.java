package com.example.inventori.Activity.Usage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestMenu;
import com.example.inventori.API.APIUsageAuto;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Adapter.AdapterUsageAuto;
import com.example.inventori.R;
import com.example.inventori.model.MenuModel;
import com.example.inventori.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventUsageAuto extends AppCompatActivity {

    ListView lvUsageAuto;
    Button btnOrder;
    AdapterUsageAuto adapterUsageAuto;
    List<MenuModel> menuModels = new ArrayList<>();
    String konfirmasi, pesanan;
    int jumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invent_usage_auto);

        lvUsageAuto = findViewById(R.id.lvUsageAuto);
        btnOrder = findViewById(R.id.btnOrder);
        getList();

        btnOrder.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(InventUsageAuto.this);
            builder.setTitle("konfirmasi pesanan");
            konfirmasi = "item: ";
            for (int i = 0; i < menuModels.size(); i++) {
                if(menuModels.get(i).getQty() != 0) {
                    pesanan = menuModels.get(i).getMenu();
                    jumlah = menuModels.get(i).getQty();
                    konfirmasi = konfirmasi + "\n" + pesanan + ": " + jumlah+"\n";
                }
            }

            builder.setMessage(konfirmasi);

            builder.setPositiveButton("Ok", (dialogInterface, i) -> {

                for (i = 0; i < menuModels.size(); i++) {
                    pesanan = menuModels.get(i).getMenu();
                    jumlah = menuModels.get(i).getQty();
                    System.out.println(pesanan +
                            " : " + jumlah);
                    konfirmOrder();
                }
                for (i = 0; i < menuModels.size(); i++) {
                    menuModels.get(i).setQty(0);
                }
                adapterUsageAuto.notifyDataSetChanged();
                Toast.makeText(InventUsageAuto.this, "pesanan dikonfirmasi",
                        Toast.LENGTH_SHORT).show();

            });
            builder.setNegativeButton(
                    "Cancel", (dialogInterface, i) -> {
                    });
            builder.show();
        });

    }

    private void getList(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> showData = dataMenu.showMenu();

        showData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String pesan = response.body().getPesan();

                menuModels = response.body().getData();
                adapterUsageAuto = new AdapterUsageAuto(InventUsageAuto.this, menuModels);
                lvUsageAuto.setAdapter(adapterUsageAuto);
                Toast.makeText(InventUsageAuto.this,
                        "success: " +"(" +code+ ")" +" "+ pesan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(InventUsageAuto.this, "error:" +t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }

    private void konfirmOrder(){
        APIUsageAuto usageData = ServerConnection.connection().create(APIUsageAuto.class);
        Call<ResponseModel> order = usageData.usage(pesanan.trim().replaceAll("\\s","_"),jumlah);

        order.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(InventUsageAuto.this, "berhasil memesan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(InventUsageAuto.this, "gagal: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}