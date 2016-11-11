package com.skysoft.slobodyanuk.timekeeper.rest;

import com.skysoft.slobodyanuk.timekeeper.rest.request.LoginRequest;
import com.skysoft.slobodyanuk.timekeeper.rest.request.SubscribeEmployeeRequest;
import com.skysoft.slobodyanuk.timekeeper.rest.request.TokenRequest;
import com.skysoft.slobodyanuk.timekeeper.rest.response.BaseResponse;
import com.skysoft.slobodyanuk.timekeeper.rest.response.EmployeesEventResponse;
import com.skysoft.slobodyanuk.timekeeper.rest.response.EmployeesResponse;
import com.skysoft.slobodyanuk.timekeeper.rest.response.LoginResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */

public interface ApiService {

    @POST("/api/subscribe/")
    Observable<BaseResponse> subscribe(@Body TokenRequest tokenRequest);

    @POST("/api/login/")
    Observable<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/users/")
    Observable<BaseResponse> subscribeEmployee(@Body SubscribeEmployeeRequest request);

    @GET("/api/employees/")
    Observable<EmployeesResponse> getEmployees();

    @GET("/api/employees/{id}")
    Observable<EmployeesResponse> getEmployeesById(@Path("id") String id);

    @GET("/api/events/{period}")
    Observable<EmployeesEventResponse> getEmployeesEvent(@Path("period") String period);


}
