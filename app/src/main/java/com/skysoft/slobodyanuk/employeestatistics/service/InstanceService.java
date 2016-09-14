package com.skysoft.slobodyanuk.employeestatistics.service;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public class InstanceService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
