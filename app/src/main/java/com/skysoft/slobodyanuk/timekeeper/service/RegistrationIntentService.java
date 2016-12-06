package com.skysoft.slobodyanuk.timekeeper.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.event.SubscribeEvent;
import com.skysoft.slobodyanuk.timekeeper.reactive.BaseTask;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.timekeeper.rest.RestClient;
import com.skysoft.slobodyanuk.timekeeper.rest.request.TokenRequest;
import com.skysoft.slobodyanuk.timekeeper.util.IllegalUrlException;
import com.skysoft.slobodyanuk.timekeeper.util.PrefsKeys;

import org.greenrobot.eventbus.EventBus;

import rx.Subscription;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public class RegistrationIntentService extends IntentService implements OnSubscribeNextListener, OnSubscribeCompleteListener {

    private static final String TAG = RegistrationIntentService.class.getCanonicalName();
    private String token;

    public RegistrationIntentService() {
        super(TAG);
    }

    private Subscription mSubscription;

    @Override
    protected void onHandleIntent(Intent intent) {
            FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
            String senderId = getResources().getString(R.string.gcm_defaultSenderId);
            token = instanceID.getToken();
            executeToken(token);
    }

    private void executeToken(String token){
        if (!TextUtils.isEmpty(token)) {
            try {
                mSubscription = new BaseTask<>()
                        .execute(this, RestClient
                                .getApiService()
                                .subscribe(new TokenRequest(token)));
            } catch (IllegalUrlException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makeToast(String message) {
        if (getApplicationContext() != null) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompleted() {
        mSubscription.unsubscribe();
    }

    @Override
    public void onError(String ex) {
        makeToast(ex);
        EventBus.getDefault().post(new SubscribeEvent(false));
        Prefs.putBoolean(PrefsKeys.SUBSCRIBE_DEVICE, false);
    }

    @Override
    public void onNext(Object t) {
        Prefs.putBoolean(PrefsKeys.SUBSCRIBE_DEVICE, true);
        EventBus.getDefault().post(new SubscribeEvent(true));
    }
}
