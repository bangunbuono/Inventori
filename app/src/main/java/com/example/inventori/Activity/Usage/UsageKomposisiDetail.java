package com.example.inventori.Activity.Usage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterOrderDetail;
import com.example.inventori.R;
import com.example.inventori.UsageAutoApplication;
import com.example.inventori.model.KomposisiModel;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.UsageMenuModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsageKomposisiDetail extends AppCompatActivity {

    ListView lvOrderDetail;
    List<UsageMenuModel> orderList;
    List<KomposisiModel> listKomposisi;
    AdapterOrderDetail adapterOrderDetail;
    Button btnCancel, btnConfirm;
    String menu, bahan, satuan, user;
    int jumlah, qty;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_komposisi_detail);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        lvOrderDetail = findViewById(R.id.lvOrderDetail);
        btnCancel = findViewById(R.id.btnCancelOrder);
        btnConfirm = findViewById(R.id.btnConfirmOrder);

        orderList = new ArrayList<>();
        orderList = UsageAutoApplication.orderList;

        adapterOrderDetail = new AdapterOrderDetail(UsageKomposisiDetail.this, orderList);
        lvOrderDetail.setAdapter(adapterOrderDetail);

        btnConfirm.setOnClickListener(view -> {
            for(int i = 0; i<orderList.size(); i++){
                menu = orderList.get(i).getMenu();
                qty = orderList.get(i).getQty();
                getKomposisi();
            }
        });

    }

    private void getKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> komposisi = dataKomposisi.getKomposisi(
                menu, user);

        komposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                listKomposisi = new ArrayList<>();
                assert response.body() != null;
                listKomposisi = response.body().getKomposisiModelList();
                String bahanTotal = "komposisi: ";
                if(listKomposisi != null){
                    for (int i = 0; i<listKomposisi.size(); i++){
                        String bahan = listKomposisi.get(i).getBahan() + " "+
                                listKomposisi.get(i).getJumlah()*qty + " " +
                                listKomposisi.get(i).getSatuan();
                        bahanTotal = bahanTotal +"\n"+ bahan;
                    }
                    System.out.println(bahanTotal);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(UsageKomposisiDetail.this, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });
    }
}