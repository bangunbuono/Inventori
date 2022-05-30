package com.example.inventori;

import android.app.Application;

import com.example.inventori.model.KomposisiModel;
import com.example.inventori.model.UsageMenuModel;

import java.util.ArrayList;

public class UsageAutoApplication extends Application {

    public static ArrayList<UsageMenuModel> orderList;
    public static ArrayList<KomposisiModel> komposisiList;

    @Override
    public void onCreate() {
        super.onCreate();
        orderList = new ArrayList<>();
        komposisiList = new ArrayList<>();
    }
}
