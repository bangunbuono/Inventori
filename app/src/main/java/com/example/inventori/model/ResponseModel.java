package com.example.inventori.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseModel {
    private int code;
    private String pesan, status;
    private List<MenuModel> data;
    private List<KomposisiModel> komposisiModelList;
    private List<StocksModel> stocksModels;
    private ArrayList<RestockModel> stocks;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<RestockModel> getStocks() {
        return stocks;
    }

    public void setRestockModel(ArrayList<RestockModel> stocks) {
        this.stocks = stocks;
    }

    public List<StocksModel> getStocksModels() {
        return stocksModels;
    }

    public void setStocksModels(List<StocksModel> stocksModels) {
        this.stocksModels = stocksModels;
    }

    public List<KomposisiModel> getKomposisiModelList() {
        return komposisiModelList;
    }

    public void setKomposisiModelList(List<KomposisiModel> komposisiModelList) {
        this.komposisiModelList = komposisiModelList;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<MenuModel> getData() {
        return data;
    }

    public void setData(List<MenuModel> data) {
        this.data = data;
    }
}
