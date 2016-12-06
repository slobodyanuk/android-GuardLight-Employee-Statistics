package com.skysoft.slobodyanuk.timekeeper.reactive;

import com.skysoft.slobodyanuk.timekeeper.rest.response.BaseResponse;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Serhii Slobodyanuk on 02.11.2016.
 */

public class BaseTask<T> {

    private Scheduler mSubscribeScheduler = Schedulers.io();
    private Scheduler mObserveScheduler = AndroidSchedulers.mainThread();

    public Subscription execute(T activity, Observable<? extends BaseResponse> observable) {
        return observable
                .retry(2)
                .subscribeOn(mSubscribeScheduler)
                .observeOn(mObserveScheduler)
                .subscribe(new BaseSubscriber<>(activity));
    }

    public void setSubscribeScheduler(Scheduler scheduler) {
        this.mSubscribeScheduler = scheduler;
    }

    public void setObserveScheduler(Scheduler scheduler) {
        this.mObserveScheduler = scheduler;
    }
}
