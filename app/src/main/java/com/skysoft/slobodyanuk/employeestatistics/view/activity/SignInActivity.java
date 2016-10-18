package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.ServerConfigurationFragment;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.SignInFragment;

public class SignInActivity extends BaseActivity {

    public static void launch(Context context) {
        Intent i = new Intent(context, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Fragment fragment = (Prefs.getBoolean(PrefsKeys.SERVER_AVAILABLE_PREF, false)) ?
                    SignInFragment.newInstance() :
                    ServerConfigurationFragment.newInstance();

            replaceFragment(R.id.container, fragment);
        }
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_singin;
    }

}
