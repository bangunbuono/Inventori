package com.example.inventori.Activity.Usage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventori.API.APIRequestStock;
import com.example.inventori.API.APIRestock;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterSpinnerRestock;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.RestockModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsageManualFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsageManualFrag extends Fragment {

    Spinner spinner;
    ArrayList<RestockModel> stockList = new ArrayList<>();
    TextView tvSatuanStock;
    EditText etJumlahStock;
    AdapterSpinnerRestock adapter;
    Button btnConfirm;
    String user;
    UserSession userSession;
    int id, jumlah;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsageManualFrag() {
        // Required empty public constructor
    }

    public static UsageManualFrag newInstance(String param1, String param2) {
        UsageManualFrag fragment = new UsageManualFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        restockList();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvSatuanStock.setText(stockList.get(i).getSatuan());
                id = stockList.get(i).getId();
//                Toast.makeText(getActivity(), "item: "+restockList.get(i).getBahan_baku(),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnConfirm.setOnClickListener(view1 -> {
            jumlah = Integer.parseInt(etJumlahStock.getText().toString().trim());
            restockAdd();
        });
        return view;
    }

    private void restockList(){
        APIRestock restockData = ServerConnection.connection().create(APIRestock.class);
        Call<ResponseModel> getData = restockData.getStock(user);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                stockList = response.body().getStocks();
                adapter = new AdapterSpinnerRestock(getActivity(), stockList);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.fillInStackTrace();
            }
        });
    }

    private void restockAdd(){
        APIRequestStock stockData = ServerConnection.connection().create(APIRequestStock.class);
        Call<ResponseModel> minusStock = stockData.minStocks(id,jumlah);

        minusStock.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                etJumlahStock.setText(null);
                Toast.makeText(getActivity(), "berhasil", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "gagal "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}