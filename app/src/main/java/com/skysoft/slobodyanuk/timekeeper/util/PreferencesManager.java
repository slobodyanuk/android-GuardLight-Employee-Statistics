package com.skysoft.slobodyanuk.timekeeper.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Serhii Slobodyanuk on 15.09.2016.
 */
public class PreferencesManager {
    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private static final String PREF_NAME = "settings";

    private static final String TEXT = "text";


    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeTaskInstance(..) method first.");
        }
        return sInstance;
    }

    public SharedPreferences getPreferences() {
        return mPref;
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }

    public void setText(String text) {
        mPref.edit()
                .putString(TEXT, text)
                .apply();
    }

    public String getText() {
        return mPref.getString(TEXT, "");
    }
}
