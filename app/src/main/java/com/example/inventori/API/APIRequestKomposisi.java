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
            @Field("table") String table,
            @Field("komposisi") String komposisi,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan
    );

    @FormUrlEncoded
    @POST("komposisiset.php")
    Call<ResponseModel> getKomposisi(
            @Field("table") String table
    );

    @FormUrlEncoded
    @POST("komposisidelete.php")
    Call<ResponseModel> deleteKomposisi(
            @Field("table") String table
    );

    @FormUrlEncoded
    @POST("komposisiupdate.php")
    Call<ResponseModel> updateKomposisi(
            @Field("table") String table,
            @Field("id") int id,
            @Field("komposisi") String komposisi,
            @Field("jumlah") int jumlah,
            @Field("satuan") String satuan
    );
}
