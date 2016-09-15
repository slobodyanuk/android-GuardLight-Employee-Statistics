package com.skysoft.slobodyanuk.employeestatistics;

import android.app.Application;

import com.skysoft.slobodyanuk.employeestatistics.util.PreferencesManager;

/**
 * Created by Serhii Slobodyanuk on 15.09.2016.
 */
public class EmployeeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.initializeInstance(this);
    }
}
