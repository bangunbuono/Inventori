package com.example.inventori.API;

import com.example.inventori.model.ResponseModel;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIReport {

    @FormUrlEncoded
    @POST("recordusage.php")
    Call<ResponseModel> recordUsage (
            @Field("kode") String kode,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("keterangan") String keterangan,
            @Field("user") String user,
            @Field("tanggal_keluar") String waktu
    );

    @FormUrlEncoded
    @POST("recordrestock.php")
    Call<ResponseModel> recordRestock (
            @Field("kode") String kode,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("user") String user,
            @Field("tanggal_masuk") String waktu
            );

    @FormUrlEncoded
    @POST("record.php")
    Call<ResponseModel> record (
            @Field("kode") String kode,
            @Field("keterangan") String keterangan,
            @Field("tanggal") String waktu,
            @Field("user") String user,
            @Field("bulan") String bulan
            );

    @FormUrlEncoded
    @POST("getmonth.php")
    Call<ResponseModel> date(
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("getdailyrecord.php")
    Call<ResponseModel> dailyRecord(
            @Field("user") String user,
            @Field("tanggal") String tanggal,
            @Field("keterangan") String keterangan
            );

    @FormUrlEncoded
    @POST("getdaterecord.php")
    Call<ResponseModel> dateList(
            @Field("user") String user,
            @Field("bulan") String bulan
    );

    @FormUrlEncoded
    @POST("getrecorddetail.php")
    Call<ResponseModel> recordDetail(
            @Field("user") String user,
            @Field("kode") String code,
            @Field("keterangan") String keterangan
    );

    @FormUrlEncoded
    @POST("statset.php")
    Call<ResponseModel> satuanList(
            @Field("user") String user
    );

}
