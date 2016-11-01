package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.service.RegistrationIntentService;
import com.skysoft.slobodyanuk.employeestatistics.util.FragmentListener;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;
import com.skysoft.slobodyanuk.employeestatistics.util.TopTabListener;
import com.skysoft.slobodyanuk.employeestatistics.view.Navigator;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.BaseFragment;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.ClockersFragment;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.EmployeeFragment;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.SettingsFragment;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.ACTIVITY;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.CLOCKERS;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.SETTINGS;

public class MainActivity extends BaseActivity implements Navigator, FragmentListener{

    @BindView(R.id.tabs)
    TabLayout mTopTabLayout;
    @BindView(R.id.main_container)
    FrameLayout mFragmentContainer;

    @BindViews({R.id.tab_clockers, R.id.tab_activity, R.id.tab_settings})
    LinearLayout[] mBottomTabLayout;

    private FragmentManager mFragmentManager;
    private EmployeeFragment mActivityFragment;
    private int mCurrentBottomTab = CLOCKERS;
    private TopTabListener callback;

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
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            if (Prefs.getBoolean(PrefsKeys.SIGN_IN_PREF, false)) {
                if (checkPlayServices()) {
                    Intent intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);
                }
                initBottomTabs();
            } else {
                SignInActivity.launch(this);
            }
        }
    }

    private void initBottomTabs() {
        switch (mCurrentBottomTab) {
            case CLOCKERS:
                onClockersClick();
                break;
            case ACTIVITY:
                onActivityClick();
                break;
            case SETTINGS:
                onSettingsClick();
                break;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    @OnClick(R.id.tab_clockers)
    public void onClockersClick() {
        mCurrentBottomTab = CLOCKERS;
        mTopTabLayout.setVisibility(GONE);
        mTopTabLayout.removeAllTabs();
        replaceFragment(R.id.main_container, ClockersFragment.newInstance(), getString(R.string.clockers));
        mBottomTabLayout[0].setSelected(true);
        mBottomTabLayout[1].setSelected(false);
        mBottomTabLayout[2].setSelected(false);
    }

    @OnClick(R.id.tab_activity)
    public void onActivityClick() {
        mCurrentBottomTab = ACTIVITY;
        Fragment fragment = EmployeeFragment.newInstance();
        callback = (TopTabListener) fragment;
        replaceFragment(R.id.main_container, fragment, getString(R.string.activity));

        mTopTabLayout.setVisibility(VISIBLE);
        mBottomTabLayout[0].setSelected(false);
        mBottomTabLayout[1].setSelected(true);
        mBottomTabLayout[2].setSelected(false);
    }

    @OnClick(R.id.tab_settings)
    public void onSettingsClick() {
        mCurrentBottomTab = SETTINGS;
        mTopTabLayout.setVisibility(GONE);
        mTopTabLayout.removeAllTabs();
        replaceFragment(R.id.main_container, SettingsFragment.newInstance(), getString(R.string.settings));
        mBottomTabLayout[0].setSelected(false);
        mBottomTabLayout[1].setSelected(false);
        mBottomTabLayout[2].setSelected(true);
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
    public void onBackPressed() {
        super.onBackPressed();
        int i = mFragmentManager.getBackStackEntryCount();
        if (i >= 1) {
            FragmentManager.BackStackEntry backEntry = mFragmentManager
                    .getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            BaseFragment currentFragment = (BaseFragment) mFragmentManager.findFragmentByTag(str);
            if (currentFragment != null) {
                currentFragment.updateToolbar();
            }
        } else {
            if (mActivityFragment != null && mActivityFragment.isVisible()) {
                mActivityFragment.updateToolbar();
            }
        }
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    public void onFragmentCreated() {
        callback.setupTabLayout(mTopTabLayout);
    }
}
