package com.skysoft.slobodyanuk.timekeeper.reactive;

import com.skysoft.slobodyanuk.timekeeper.rest.response.BaseResponse;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Serhii Slobodyanuk on 02.11.2016.
 */

public class BaseTask<T> {

    public Subscription execute(T activity, Observable<? extends BaseResponse> observable){
      return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<>(activity));
    }
}
