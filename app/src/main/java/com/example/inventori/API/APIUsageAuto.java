package com.example.inventori.API;

import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIUsageAuto {
    @FormUrlEncoded
    @POST("usageauto.php")
    Call<ResponseModel> usage(
            @Field("table") String table,
            @Field("qty") int qty
    );
}
