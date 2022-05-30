package com.example.inventori.API;

import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIRequestKomposisi {

    @FormUrlEncoded
    @POST("tablemenu.php")
    Call<ResponseModel> createTable(
            @Field("table") String table
    );

    @FormUrlEncoded
    @POST("komposisiadd.php")
    Call<ResponseModel> addKomposisi(
            @Field("user") String user,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan,
            @Field("menu") String menu
    );

    @FormUrlEncoded
    @POST("komposisiset.php")
    Call<ResponseModel> getKomposisi(
            @Field("menu") String menu,
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("komposisidelete.php")
    Call<ResponseModel> deleteKomposisi(
            @Field("table") String table
    );

    @FormUrlEncoded
    @POST("komposisiupdate.php")
    Call<ResponseModel> updateKomposisi(
            @Field("id") int id,
            @Field("bahan") String bahan,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan
    );
}
