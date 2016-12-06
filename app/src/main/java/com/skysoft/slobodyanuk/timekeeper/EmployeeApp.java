package com.skysoft.slobodyanuk.timekeeper;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.timekeeper.util.PreferencesManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Serhii Slobodyanuk on 15.09.2016.
 */
public class EmployeeApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.initializeInstance(this);
        context = getApplicationContext();

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
//        Realm.deleteRealm(realmConfiguration);
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static Context getContext(){
        return context;
    }
}
