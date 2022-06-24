package com.example.inventori.Activity.Usage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.inventori.Activity.User.UserSession;
import com.example.inventori.Adapter.AdapterOrderDetail;
import com.example.inventori.R;
import com.example.inventori.UsageAutoApplication;
import com.example.inventori.model.UsageMenuModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UsageKomposisiDetail extends AppCompatActivity {

    ListView lvOrderDetail;
    TextView tvOrder;
    List<UsageMenuModel> orderList;
    AdapterOrderDetail adapterOrderDetail;
    Button btnCancel;
    public static Button btnConfirm;
    LocalDateTime time;
    String user, date;
    public static String orderSeries;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_komposisi_detail);

        userSession = new UserSession(getApplicationContext());
        user = userSession.getUserDetail().get("username");

        lvOrderDetail = findViewById(R.id.lvOrderDetail);
        tvOrder = findViewById(R.id.tvOrderDetail);
        btnCancel = findViewById(R.id.btnCancelOrder);
        btnConfirm = findViewById(R.id.btnConfirmOrder);

        orderList = new ArrayList<>();
        orderList = UsageAutoApplication.orderList;

        adapterOrderDetail = new AdapterOrderDetail(UsageKomposisiDetail.this, orderList);
        lvOrderDetail.setAdapter(adapterOrderDetail);

        time = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddhhmmss");
        date = dtf.format(time);
        orderSeries = "No. " +"B."+date;

        tvOrder.setText(orderSeries);
    }
}