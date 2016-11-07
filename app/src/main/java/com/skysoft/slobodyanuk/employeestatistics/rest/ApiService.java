package com.skysoft.slobodyanuk.employeestatistics.rest;

import com.skysoft.slobodyanuk.employeestatistics.rest.request.LoginRequest;
import com.skysoft.slobodyanuk.employeestatistics.rest.request.TokenRequest;
import com.skysoft.slobodyanuk.employeestatistics.rest.response.BaseResponse;
import com.skysoft.slobodyanuk.employeestatistics.rest.response.EmployeesResponse;
import com.skysoft.slobodyanuk.employeestatistics.rest.response.LoginResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public interface ApiService {

    @POST("/api/subscribe/")
    Observable<BaseResponse> sendToken(@Body TokenRequest tokenRequest);

    @POST("/api/login/")
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET("/api/employees/")
    Observable<EmployeesResponse> getEmployees();

}
