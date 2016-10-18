package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.service.RegistrationIntentService;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;
import com.skysoft.slobodyanuk.employeestatistics.view.Navigator;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.EmployeeFragment;

public class MainActivity extends BaseActivity implements Navigator {

    public static void launch(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (Prefs.getBoolean(PrefsKeys.SIGN_IN_PREF, false)) {
                if (checkPlayServices()) {
                    Intent intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);
                }
                replaceFragment(R.id.container, EmployeeFragment.newInstance());
            } else {
                SignInActivity.launch(this);
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, Globals.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    public void navigateToEmployeeStatistics(String id) {

    }
}
