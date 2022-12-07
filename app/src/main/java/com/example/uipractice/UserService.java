package com.example.uipractice;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    @POST("/api-token-auth/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);


    @GET("/deviceinfo/")
    Call<IpResponse> retrieveIp(@Header("Authorization") String auth);

    final OkHttpClient okHttpClient1 = new OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit1 = new Retrofit.Builder()
            .baseUrl(Constants.LOGIN_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient1)
            .build();
    UserService service = retrofit1.create(UserService.class);
}
