package com.example.inventori.API;

import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestStock {
    @GET("stockset.php")
    Call<ResponseModel> showStock();

    @FormUrlEncoded
    @POST("stockdetail.php")
    Call<ResponseModel> showDetail(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("stockupdate.php")
    Call<ResponseModel> updateData(
            @Field("id") int id,
            @Field("bahan_baku") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("min_pesan") int min_pesan,
            @Field("waktu") int waktu
    );

    @FormUrlEncoded
    @POST("stockadd.php")
    Call<ResponseModel> addData(
            @Field("bahan_baku") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("min_pesan") int min_pesan,
            @Field("waktu") int waktu
    );
}
