package com.example.inventori.Activity.Report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inventori.API.APIReport;
import com.example.inventori.API.ServerConnection;
import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.RVAdapter.AdapterStat;
import com.example.inventori.R;
import com.example.inventori.model.ResponseModel;
import com.example.inventori.model.StatModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatFrag extends Fragment {
    RecyclerView rvListSatuan;
    AdapterStat adapterStat;
    RecyclerView.LayoutManager layoutManager;
    UserSession session;
    private String user;
    List<StatModel> list = new ArrayList<>();

    public StatFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new UserSession(getActivity());
        user = session.getUserDetail().get("username");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat, container, false);
        rvListSatuan = view.findViewById(R.id.rvListSatuan);

        getList();
        return view;
    }

    private void getList(){
        APIReport record = ServerConnection.connection().create(APIReport.class);
        Call<ResponseModel> recordData = record.satuanList(user);

        recordData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                list = response.body() != null ? response.body().getStatmodel() : null;
                adapterStat = new AdapterStat(getActivity(), list);
                layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                rvListSatuan.setLayoutManager(layoutManager);
                if(list!=null) {
                    rvListSatuan.setAdapter(adapterStat);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {

            }
        });

    }

}
