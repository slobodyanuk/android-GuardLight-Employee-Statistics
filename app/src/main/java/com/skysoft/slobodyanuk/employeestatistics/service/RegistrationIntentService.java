package com.skysoft.slobodyanuk.employeestatistics.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.rest.RestClient;
import com.skysoft.slobodyanuk.employeestatistics.rest.request.TokenRequest;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = RegistrationIntentService.class.getCanonicalName();

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        String token = instanceID.getToken();
        Log.d(TAG, "FCM Registration Token: " + token);

        if (!TextUtils.isEmpty(token)) {
            Observable<Response<String>> tokenObservable = RestClient.getApiService().sendToken(new TokenRequest(token));
            tokenObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<String>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            makeToast("Token failed : " + e.toString());
                            Log.e(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onNext(Response<String> tokenResponse) {
                            String message = (tokenResponse.isSuccessful())
                                    ? "Token sent successful"
                                    : "Token failed";
                            makeToast(message);
                        }
                    });
        }
    }

    private void makeToast(String message){
        if (getApplicationContext() != null) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
