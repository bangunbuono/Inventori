package com.example.inventori.API;

import com.example.inventori.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIAccounts {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseModel> login(
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseModel> register(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("registermanager.php")
    Call<ResponseModel> registerManager(
            @Field("manager_name") String managerName,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("loginmanager.php")
    Call<ResponseModel> loginManager(
            @Field("manager_name") String username,
            @Field("password") String password
    );
}
