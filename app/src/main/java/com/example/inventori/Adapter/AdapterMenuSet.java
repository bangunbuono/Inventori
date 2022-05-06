package com.example.inventori.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.APIRequestMenu;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.Menu.MenuSet;
import com.example.inventori.Activity.Menu.MenuSetDetail;
import com.example.inventori.R;
import com.example.inventori.model.MenuModel;
import com.example.inventori.model.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMenuSet extends ArrayAdapter<MenuModel> {
    private Context context;
    private List<MenuModel> listMenu;
    private List<MenuModel> menu;
    public TextView tvIdMenu, tvMenu;
    int index;

    public AdapterMenuSet(Context context, List<MenuModel> list) {
        super(context, R.layout.menu_row,list);
        this.context = context;
        listMenu = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.menu_row, parent, false);

        tvMenu =convertView.findViewById(R.id.tvMenu);
        tvIdMenu = convertView.findViewById(R.id.tvIdMenu);

        tvMenu.setText(listMenu.get(position).getMenu());
        tvIdMenu.setText(listMenu.get(position).getId()+"");


        convertView.setOnClickListener(view -> {
            index = listMenu.get(position).getId();
            detailMenu();
        });

        convertView.setOnLongClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            index = listMenu.get(position).getId();
            builder.setMessage(index+". "+tvMenu.getText().toString());
            builder.setPositiveButton("Hapus", (dialogInterface, i) -> {
                deleteMenu();
                deleteKomposisi();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((MenuSet)context).retrieveData();
                    }
                }, 500);
            });

            builder.setNegativeButton("ubah", (dialogInterface, i) -> detailMenu());
            builder.show();
            return false;
        });

        return convertView;

    }
    private void deleteMenu(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> deleteData = dataMenu.deleteMenu(index);
        
        deleteData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(context, "Menu berhasil dihapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(context, "gagal menghapus data"+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteKomposisi(){
        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> hapusKomposisi = dataKomposisi.deleteKomposisi(
                tvMenu.getText().toString().trim().replaceAll("\\s","_"));
        hapusKomposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Toast.makeText(context, "komposisi berhasil dihapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(context, "komposisi gagal dihapus:"+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void detailMenu(){
        APIRequestMenu dataMenu = ServerConnection.connection().create(APIRequestMenu.class);
        Call<ResponseModel> detailData = dataMenu.detailMenu(index);

        detailData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                menu = response.body().getData();

                int menuId = menu.get(0).getId();
                String menuName = menu.get(0).getMenu();
                int menuPrice = menu.get(0).getHarga();
                String menuDesc = menu.get(0).getDeskripsi();

                Intent intent = new Intent(context, MenuSetDetail.class);
                intent.putExtra("menuId", menuId);
                intent.putExtra("menuName", menuName);
                intent.putExtra("menuPrice", menuPrice);
                intent.putExtra("menuDesc", menuDesc);
                context.startActivity(intent);

                System.out.println(menuId +" "+menuName+ " "+menuPrice+ " "+menuDesc);

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(context, "gagal menghapus data"+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
