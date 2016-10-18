package com.skysoft.slobodyanuk.employeestatistics;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.util.PreferencesManager;

/**
 * Created by Serhii Slobodyanuk on 15.09.2016.
 */
public class EmployeeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.initializeInstance(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
