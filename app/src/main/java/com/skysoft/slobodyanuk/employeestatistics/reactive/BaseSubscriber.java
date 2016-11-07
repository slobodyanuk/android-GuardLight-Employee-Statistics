package com.skysoft.slobodyanuk.employeestatistics.reactive;

import com.google.gson.Gson;
import com.skysoft.slobodyanuk.employeestatistics.rest.response.BaseResponse;
import com.skysoft.slobodyanuk.employeestatistics.rest.response.ErrorResponse;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class BaseSubscriber<T> extends Subscriber<BaseResponse> {

    private OnSubscribeCompleteListener completeListener;
    private OnSubscribeNextListener nextListener;

    public BaseSubscriber(T listener) {
        this.completeListener = (OnSubscribeCompleteListener) listener;
        this.nextListener = (OnSubscribeNextListener) listener;
    }

    @Override
    public void onCompleted() {
        completeListener.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            Response response = exception.response();
            Gson gson = new Gson();
            try {
                completeListener.onError(gson
                        .fromJson(response.errorBody().string(), ErrorResponse.class).getMessage());
            } catch (IOException e1) {
                completeListener.onError("Failed");
            }
        } else {
            completeListener.onError(e.getMessage());
        }
    }

    @Override
    public void onNext(BaseResponse t) {
        nextListener.onNext(t);
    }
}