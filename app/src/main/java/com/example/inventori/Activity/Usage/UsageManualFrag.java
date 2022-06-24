package com.example.inventori.Activity.Usage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.APIRestock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterKomposisi;
import com.example.inventori.Adapter.AdapterSpinnerRestock;
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
    ArrayList<Integer> listId;
    ListView lvManualUsage;
    AdapterKomposisi adapterKomposisi;
    TextView tvSatuanStock;
    EditText etJumlahStock;
    AdapterSpinnerRestock adapter;
    Button btnConfirm, btnAddManualList;
    String user, bahan, satuan;
    UserSession userSession;
    int id, jumlah;

    public UsageManualFrag() {
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

        spinner = view.findViewById(R.id.spinnerManual);
        tvSatuanStock = view.findViewById(R.id.tvManual);
        btnConfirm = view.findViewById(R.id.btnManual);
        etJumlahStock = view.findViewById(R.id.etManual);
        btnAddManualList = view.findViewById(R.id.btnAddManualList);
        lvManualUsage = view.findViewById(R.id.lvManualUsage);

        manualUsageList = new ArrayList<>();
        listId = new ArrayList<>();
        adapterKomposisi = new AdapterKomposisi(getActivity(), manualUsageList);

        if(manualUsageList!=null){
            lvManualUsage.setAdapter(adapterKomposisi);
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
                    adapterKomposisi.notifyDataSetChanged();
                    etJumlahStock.setText(null);
                    spinner.setSelection(0);
                }
                else {
                    Toast.makeText(getActivity(), "Bahan sudah ada", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnConfirm.setOnClickListener(view1 -> {

            //restockAdd();
        });
        return view;
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

    private void restockAdd(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> minusStock = stockData.minStocks(id,jumlah);

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