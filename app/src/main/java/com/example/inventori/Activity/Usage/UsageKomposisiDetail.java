package com.example.inventori.Activity.Usage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inventori.Adapter.AdapterOrderDetail;
import com.example.inventori.R;
import com.example.inventori.UsageAutoApplication;
import com.example.inventori.model.UsageMenuModel;

import java.util.ArrayList;
import java.util.List;

public class UsageKomposisiDetail extends AppCompatActivity {

    ListView lvOrderDetail;
    List<UsageMenuModel> orderList;
    AdapterOrderDetail adapterOrderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_komposisi_detail);

        lvOrderDetail = findViewById(R.id.lvOrderDetail);

        orderList = new ArrayList<>();
        orderList = UsageAutoApplication.orderList;

        adapterOrderDetail = new AdapterOrderDetail(UsageKomposisiDetail.this, orderList);
        lvOrderDetail.setAdapter(adapterOrderDetail);



    }
}