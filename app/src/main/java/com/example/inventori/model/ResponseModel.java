package com.example.inventori.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseModel {
    private int code;
    private String pesan, status;
    private List<MenuModel> data;
    private List<KomposisiModel> komposisiModelList, komposisiOpsiList;
    private List<MonthModel> bulan;
    private List<RecordModel> record, recordDetail;
    private List<StatModel> statmodel;

    public List<StatModel> getStatmodel() {
        return statmodel;
    }

    public void setStatmodel(List<StatModel> statmodel) {
        this.statmodel = statmodel;
    }

    public void setStocks(ArrayList<RestockModel> stocks) {
        this.stocks = stocks;
    }

    public List<RecordModel> getRecordDetail() {
        return recordDetail;
    }

    public void setRecordDetail(List<RecordModel> recordDetail) {
        this.recordDetail = recordDetail;
    }

    public List<RecordModel> getDate() {
        return date;
    }

    public void setDate(List<RecordModel> date) {
        this.date = date;
    }

    private List<RecordModel> date;

    public List<RecordModel> getRecord() {
        return record;
    }

    public void setRecord(List<RecordModel> record) {
        this.record = record;
    }

    public List<MonthModel> getBulan() {
        return bulan;
    }

    public void setBulan(List<MonthModel> bulan) {
        this.bulan = bulan;
    }

    public List<KomposisiModel> getKomposisiOpsiList() {
        return komposisiOpsiList;
    }

    public void setKomposisiOpsiList(List<KomposisiModel> komposisiOpsiList) {
        this.komposisiOpsiList = komposisiOpsiList;
    }

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
