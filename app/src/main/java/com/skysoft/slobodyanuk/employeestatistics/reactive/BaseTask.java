package com.skysoft.slobodyanuk.employeestatistics.reactive;

import com.skysoft.slobodyanuk.employeestatistics.rest.response.BaseResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Serhii Slobodyanuk on 02.11.2016.
 */

public class BaseTask<T> {

    public void execute(T activity, Observable<? extends BaseResponse> observable){
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<>(activity));
    }
}
