package com.example.inventori.API;

import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRestock {
    @FormUrlEncoded
    @POST("restocklist.php")
    Call<ResponseModel> getStock(
            @Field("user") String user
    );
}
