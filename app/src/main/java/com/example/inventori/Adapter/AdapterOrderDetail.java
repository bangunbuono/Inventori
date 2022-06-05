package com.example.inventori.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
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

public class AdapterOrderDetail extends ArrayAdapter<UsageMenuModel> {

    Context context;
    List<UsageMenuModel> orderList;
    List<KomposisiModel> listKomposisi = new ArrayList<>();
    String user;
    UserSession userSession;

    public AdapterOrderDetail(@NonNull Context context, @NonNull List<UsageMenuModel> objects) {
        super(context, R.layout.order_detail_row, objects);
        this.context = context;
        orderList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userSession = new UserSession(context);
        user = userSession.getUserDetail().get("username");
        convertView = inflater.inflate(R.layout.order_detail_row, parent, false);

        TextView tvMenuOrder = convertView.findViewById(R.id.tvMenuOrder);
        TextView tvQtyOrder = convertView.findViewById(R.id.tvQtyOrder);
        TextView tvDeskripsiOrder = convertView.findViewById(R.id.tvDeskripsiOrder);

        tvMenuOrder.setText(UsageAutoApplication.orderList.get(position).getMenu());
        tvQtyOrder.setText(UsageAutoApplication.orderList.get(position).getQty()+"");

        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> komposisi = dataKomposisi.getKomposisi(
                tvMenuOrder.getText().toString().trim(), user);

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
                                listKomposisi.get(i).getJumlah() + " " +
                                listKomposisi.get(i).getSatuan();
                        bahanTotal = bahanTotal +"\n"+ bahan;
                    }
                    tvDeskripsiOrder.setText(bahanTotal);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

        return convertView;

    }
}
