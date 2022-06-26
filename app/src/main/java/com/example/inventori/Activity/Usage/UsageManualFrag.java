package com.example.inventori.Activity.Usage;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.APIRestock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterSpinnerRestock;
import com.example.inventori.Adapter.AdapterUsageManualFrag;
import com.example.inventori.R;
import com.example.inventori.model.KomposisiModel;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.RestockModel;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsageManualFrag extends Fragment {

    Spinner spinner;
    ArrayList<RestockModel> stockList = new ArrayList<>();
    ArrayList<KomposisiModel> manualUsageList;
    public static ArrayList<Integer> listId;
    ListView lvManualUsage;
    AdapterUsageManualFrag adapterList;
    TextView tvSatuanStock;
    @SuppressLint("StaticFieldLeak")
    public static TextView totalItem;
    EditText etJumlahStock;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout layoutItem;
    AdapterSpinnerRestock adapter;
    Button btnConfirm, btnAddManualList;
    String user, bahan, satuan;
    UserSession userSession;
    int id, jumlah, idx, jumlahx;

    public UsageManualFrag() {
        checkItem();
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userSession = new UserSession(getActivity());
        user = userSession.getUserDetail().get("username");

        View view = inflater.inflate(R.layout.fragment_usage_manual, container, false);

        layoutItem = view.findViewById(R.id.layoutItemManual);
        spinner = view.findViewById(R.id.spinnerManual);
        tvSatuanStock = view.findViewById(R.id.tvManual);
        btnConfirm = view.findViewById(R.id.btnManual);
        etJumlahStock = view.findViewById(R.id.etManual);
        btnAddManualList = view.findViewById(R.id.btnAddManualList);
        lvManualUsage = view.findViewById(R.id.lvManualUsage);
        totalItem = view.findViewById(R.id.tvTotalStockManual);

        manualUsageList = new ArrayList<>();
        listId = new ArrayList<>();
        adapterList = new AdapterUsageManualFrag(getActivity(), manualUsageList);

        if(manualUsageList!=null){
            lvManualUsage.setAdapter(adapterList);
        }

        restockList();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvSatuanStock.setText(stockList.get(i).getSatuan());
                id = stockList.get(i).getId();
                bahan = stockList.get(i).getBahan_baku();
                satuan = stockList.get(i).getSatuan();
//                Toast.makeText(getActivity(), "item: "+restockList.get(i).getBahan_baku(),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnAddManualList.setOnClickListener(view1 -> {
            if(etJumlahStock.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Isi jumlah dulu", Toast.LENGTH_SHORT).show();
            }else {
                if (!listId.contains(id)){
                    jumlah = Integer.parseInt(etJumlahStock.getText().toString().trim());
                    manualUsageList.add(new KomposisiModel(id, bahan, satuan, jumlah));
                    listId.add(id);
                    adapterList.notifyDataSetChanged();
                    etJumlahStock.setText(null);
                    spinner.setSelection(0);
                    checkItem();
                }
                else {
                    Toast.makeText(getActivity(), "Bahan sudah ada", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnConfirm.setOnClickListener(view1 -> {
            if(manualUsageList!= null){
                for(int i =0; i<manualUsageList.size();i++){
                    idx = manualUsageList.get(i).getId();
                    jumlahx = manualUsageList.get(i).getJumlah();
                    stockUsage();
                }
            }
            getActivity().finish();

        });
        return view;
    }

    public void checkItem(){
        if(manualUsageList!=null){
            if(manualUsageList.size()!=0){
                layoutItem.setVisibility(View.VISIBLE);
                totalItem.setText("Total " + manualUsageList.size() + " item");
            }
            else {
                layoutItem.setVisibility(View.GONE);
            }
        }
    }

    private void restockList(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call,@NonNull Response<ResponseModel> response) {
                assert response.body() != null;
                stockList = response.body().getStocks();
                adapter = new AdapterSpinnerRestock(Objects.requireNonNull(getActivity()), stockList);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    private void stockUsage(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> minusStock = stockData.minStocks(idx,jumlahx);

        minusStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull retrofit2.Response<ResponseModel> response) {
                etJumlahStock.setText(null);
                Toast.makeText(getActivity(), "berhasil", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "gagal "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}