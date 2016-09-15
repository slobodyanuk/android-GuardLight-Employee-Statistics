package com.skysoft.slobodyanuk.employeestatistics.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public class RestClient {

    private static String BASE_API_URL = "http://192.168.1.150:3000";

    private static final OkHttpClient client;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .setLenient()
            .create();

    private static final Retrofit mRestAdapter = new Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build();

    private static final ApiService mApiService = mRestAdapter.create(ApiService.class);

    public static ApiService getApiService() {
        return mApiService;
    }
}
