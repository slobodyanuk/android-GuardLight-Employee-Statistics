package com.skysoft.slobodyanuk.employeestatistics.reactive;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public abstract class BaseTask<T> {

    private Subscription mSubscription;

    @SuppressWarnings("unchecked")
    public void execute(final Subscriber taskSubscriber) {
//        unsubscribe();
//        Observable taskObservable = createTaskObservable();
//        this.mSubscription = taskObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(taskSubscriber);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

//    protected Observable createTaskObservable() {
//        return Observable.create(this :: executeTask);
//    }

    protected abstract void executeTask(Subscriber<? super T> subscriber);

}
