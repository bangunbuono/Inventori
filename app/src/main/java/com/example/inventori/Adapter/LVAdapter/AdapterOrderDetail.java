package com.example.inventori.Adapter.LVAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.inventori.API.APIKomposisiOpsi;
import com.example.inventori.API.APIReport;
import com.example.inventori.API.APIRequestKomposisi;
import com.example.inventori.API.APIUsageAuto;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.Usage.UsageKomposisiDetail;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.SpinnerAdapter.AdapterSpinnerDetailOrderOpsi;
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
    ArrayList<KomposisiModel> opsiList;
    ArrayList<String> bahanArray;
    AdapterSpinnerDetailOrderOpsi adapterSpinnerBahan;
    String user, menu, opsi, opsiSatuan;
    int qty, opsiJumlah;
    UserSession userSession;
    UsageAutoApplication usageAutoApplication;


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

        usageAutoApplication = new UsageAutoApplication();
        opsiList = new ArrayList<>();
        bahanArray = new ArrayList<>();

        for (int i = 0; i < orderList.size(); i++) {
            opsiList.add(new KomposisiModel(-3, null, null, 0));
            bahanArray.add(null);
        }

        TextView tvMenuOrder = convertView.findViewById(R.id.tvMenuOrder);
        TextView tvQtyOrder = convertView.findViewById(R.id.tvQtyOrder);
        TextView tvDeskripsiOrder = convertView.findViewById(R.id.tvDeskripsiOrder);
        Spinner spinnerBahan = convertView.findViewById(R.id.spinnerBahanDeskripsi);

        tvMenuOrder.setText(UsageAutoApplication.orderList.get(position).getMenu());
        tvQtyOrder.setText(UsageAutoApplication.orderList.get(position).getQty() + "");

        APIRequestKomposisi dataKomposisi = ServerConnection.connection().create(APIRequestKomposisi.class);
        Call<ResponseModel> komposisi = dataKomposisi.getKomposisi(
                tvMenuOrder.getText().toString().trim(), user);

        komposisi.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                listKomposisi = new ArrayList<>();
                assert response.body() != null;
                listKomposisi = response.body().getKomposisiModelList();
                String bahanTotal = "komposisi utama: ";
                if (listKomposisi != null) {
                    for (int i = 0; i < listKomposisi.size(); i++) {
                        String bahan = listKomposisi.get(i).getBahan() + " " +
                                listKomposisi.get(i).getJumlah() + " " +
                                listKomposisi.get(i).getSatuan();
                        bahanTotal = bahanTotal + "\n" + bahan;
                    }
                    tvDeskripsiOrder.setText(bahanTotal);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "gagal memuat: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

        APIKomposisiOpsi komposisiOpsiData = ServerConnection.connection().create(APIKomposisiOpsi.class);
        Call<ResponseModel> getData = komposisiOpsiData.getKomposisi(tvMenuOrder.getText().toString().trim(), user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                List<KomposisiModel> namaBahan, listBahan;
                listBahan = new ArrayList<>();
                listBahan.add(0, new KomposisiModel(-1, "Pilih bahan", "", 0));
                namaBahan = response.body().getKomposisiOpsiList();
                if(namaBahan != null){
                    opsiList.set(position, new KomposisiModel(-1, null,null, 0));
                    bahanArray.set(position, "ada");
                    for (int i = 0; i < namaBahan.size(); i++) {
                        listBahan.add(new KomposisiModel(namaBahan.get(i).getId(), namaBahan.get(i).getBahan(),
                                namaBahan.get(i).getSatuan(), namaBahan.get(i).getJumlah()));
                    }
                }else{
                    opsiList.set(position, new KomposisiModel(-2, null, null, 0));
                    bahanArray.set(position, "ga ada");
                }


                if (listBahan.size()>1) {
                    adapterSpinnerBahan = new AdapterSpinnerDetailOrderOpsi(context, listBahan);
                    spinnerBahan.setAdapter(adapterSpinnerBahan);

                    spinnerBahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(!listBahan.get(i).getBahan().equals("Pilih bahan")){
                                String bahan = listBahan.get(i).getBahan();
                                String satuan = listBahan.get(i).getSatuan();
                                int jumlah = listBahan.get(i).getJumlah();

                                opsiList.set(position, new KomposisiModel(position, bahan, satuan,jumlah));
                                bahanArray.set(position, bahan);

                            }else if(listBahan.get(i).getBahan().equals("Pilih bahan")){
                                opsiList.set(position, new KomposisiModel(position, "Pilih bahan", null, 0));
                                bahanArray.set(position, "ada");
                                Toast.makeText(context, "Pilih bahan dulu", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });

                }
                else if (listBahan.size()==1){
                    spinnerBahan.setVisibility(View.GONE);
                    spinnerBahan.setActivated(false);
                    spinnerBahan.setEnabled(false);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                t.fillInStackTrace();
            }
        });

        UsageKomposisiDetail.btnConfirm.setOnClickListener(view -> {
            int i;
            if(opsiList.size()==0){
                record();
                for(i=0; i<orderList.size(); i++){
                    menu = orderList.get(i).getMenu();
                    qty = orderList.get(i).getQty();
                    confirmMainOrder();
                    getKomposisi();
                    Toast.makeText(context, "berhasil memesan", Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                }
            }
            else{
                if(bahanArray.contains("ada")){
                    Toast.makeText(context, "Pilih bahan dulu", Toast.LENGTH_SHORT).show();
                }else {
                    record();
                    for(i = 0; i<orderList.size(); i++){
                        menu = orderList.get(i).getMenu();
                        qty = orderList.get(i).getQty();
                        confirmMainOrder();
                        if(opsiList.get(i).getId()!=-2){
                            opsi = opsiList.get(i).getBahan();
                            opsiJumlah = opsiList.get(i).getJumlah();
                            opsiSatuan = opsiList.get(i).getSatuan();
                            confirmOptionOrder();
                            recordOpsi();
                            System.out.println(opsiList.get(i).getBahan() + " " +
                                    opsiList.get(i).getJumlah() + " " + opsiList.get(i).getSatuan());
                        }
                        getKomposisi();
                    }
                    Toast.makeText(context, "berhasil memesan", Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                }
            }
        });

        return convertView;

    }

    private void confirmMainOrder(){
        APIUsageAuto usageData = ServerConnection.connection().create(APIUsageAuto.class);
        Call<ResponseModel> order = usageData.usage(menu, user);

        order.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void confirmOptionOrder(){
        APIUsageAuto dataUsage = ServerConnection.connection().create(APIUsageAuto.class);
        Call<ResponseModel> confirmOption = dataUsage.usageOpsi(menu, user, opsi);

        confirmOption.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void recordOpsi(){
        APIReport reportData = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> reportUsage = reportData.recordUsage(
                UsageKomposisiDetail.orderSeries, opsi, opsiJumlah, opsiSatuan,
                "auto(opsi)", user, UsageKomposisiDetail.formatedTime);
        reportUsage.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {

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

                //sout untuk test doang
                String bahanTotal = "komposisi: ";
                if(listKomposisi != null){
                    for (int i = 0; i<listKomposisi.size(); i++){
                        APIReport reportData = ServerConnection.connection().create(APIReport.class);

                        Call<ResponseModel> reportUsage = reportData.recordUsage(
                                UsageKomposisiDetail.orderSeries,listKomposisi.get(i).getBahan(),
                                listKomposisi.get(i).getJumlah(), listKomposisi.get(i).getSatuan(),
                                "auto(utama)", user, UsageKomposisiDetail.formatedTime);
                        reportUsage.enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseModel> call,@NonNull Throwable t) {

                            }
                        });

                        String bahan = listKomposisi.get(i).getBahan() + " "+
                                listKomposisi.get(i).getJumlah() + " " +
                                listKomposisi.get(i).getSatuan();

                        bahanTotal = bahanTotal +"\n"+ bahan;
                    }
                    System.out.println(bahanTotal);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(context, "gagal memuat: "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void record(){
        APIReport recordData = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> record = recordData.record(
                UsageKomposisiDetail.orderSeries,"barang keluar",
                UsageKomposisiDetail.formatedTime, user, UsageKomposisiDetail.month);

        record.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });
    }
}