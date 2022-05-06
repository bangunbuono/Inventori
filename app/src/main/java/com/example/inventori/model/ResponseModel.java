package com.example.inventori.model;

import java.util.List;

public class ResponseModel {
    private int code;
    private String pesan;
    private List<MenuModel> data;
    private List<KomposisiModel> komposisiModelList;
    private List<StocksModel> stocksModels;

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
