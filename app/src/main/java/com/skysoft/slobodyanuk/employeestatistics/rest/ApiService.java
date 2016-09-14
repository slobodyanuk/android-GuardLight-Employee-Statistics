package com.skysoft.slobodyanuk.employeestatistics.rest;

import com.skysoft.slobodyanuk.employeestatistics.rest.request.TokenRequest;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public interface ApiService {

    @POST("/settoken")
    Observable<Response<String>> sendToken(@Body TokenRequest tokenRequest);

}
