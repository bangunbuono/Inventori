package com.example.inventori.model;

public class StocksModel {
    private int id;
    private int waktu;
    private int min_pesan;
    private int jumlah;
    private String bahan_baku, satuan;

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWaktu() {
        return waktu;
    }

    public void setWaktu(int waktu) {
        this.waktu = waktu;
    }

    public int getMin_pesan() {
        return min_pesan;
    }

    public void setMin_pesan(int min_pesan) {
        this.min_pesan = min_pesan;
    }

    public String getBahan_baku() {
        return bahan_baku;
    }

    public void setBahan_baku(String bahan_baku) {
        this.bahan_baku = bahan_baku;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}