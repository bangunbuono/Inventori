package com.example.inventori.model;

public class KomposisiModel {
    private String komposisi,satuan;
    private int jumlah, id;

    public KomposisiModel(int id, String komposisi, String satuan, int jumlah) {
        this.komposisi = komposisi;
        this.satuan = satuan;
        this.jumlah = jumlah;
        this.id = id;
    }

    public String getKomposisi() {
        return komposisi;
    }

    public void setKomposisi(String komposisi) {
        this.komposisi = komposisi;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

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
}
