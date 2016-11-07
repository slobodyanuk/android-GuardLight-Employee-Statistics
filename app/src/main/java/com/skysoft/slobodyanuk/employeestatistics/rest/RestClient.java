package com.skysoft.slobodyanuk.employeestatistics.rest;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.util.IllegalUrlException;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public class RestClient {

    private static final OkHttpClient client;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("x-access-token", Prefs.getString(PrefsKeys.API_KEY, ""))
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }).addInterceptor(interceptor).build();
    }

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
            .setLenient()
            .create();

    public static ApiService getApiService() throws IllegalUrlException {
        String url = Prefs.getString(PrefsKeys.SERVER_URL, "");
        if (!TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches()) {
            return new Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()
                    .create(ApiService.class);
        } else {
            throw new IllegalUrlException();
        }
    }
}
