package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.service.RegistrationIntentService;
import com.skysoft.slobodyanuk.employeestatistics.util.FragmentListener;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;
import com.skysoft.slobodyanuk.employeestatistics.util.KeyboardUtil;
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

public class MainActivity extends BaseActivity implements Navigator, FragmentListener {

    @BindView(R.id.tabs)
    TabLayout mTopTabLayout;
    @BindView(R.id.main_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.bottom_tab_container)
    FrameLayout mBottomTabContainer;
    @BindView(R.id.root)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.bar_layout)
    AppBarLayout mAppBarLayout;

    @BindViews({R.id.tab_clockers, R.id.tab_activity, R.id.tab_settings})
    LinearLayout[] mBottomTabLayout;

    private FragmentManager mFragmentManager;
    private int mCurrentBottomTab = -1;

    public static void launch(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Globals.PAGE_KEY, mCurrentBottomTab);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentBottomTab = savedInstanceState.getInt(Globals.PAGE_KEY);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            if (Prefs.getBoolean(PrefsKeys.SIGN_IN, false) &&
                    !TextUtils.isEmpty(Prefs.getString(PrefsKeys.API_KEY, ""))) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
                initBottomTabs();
            } else {
                Toast.makeText(this, getString(R.string.sign_in), Toast.LENGTH_SHORT).show();
                SignInActivity.launch(this);
            }
        } else {
            initBottomTabs();
        }
        setupCoordinatorLayout();
    }

    private void setupCoordinatorLayout() {
        mCoordinatorLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int heightDiff = mCoordinatorLayout.getRootView().getHeight() - mCoordinatorLayout.getHeight();
            if (heightDiff > KeyboardUtil.dpToPx(MainActivity.this, 200)) {
                changeBottomTabVisibility(true);
            } else {
                changeBottomTabVisibility(false);
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
                AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
                if (behavior != null) {
                    behavior.onNestedFling(mCoordinatorLayout, mAppBarLayout, null, 0, -10000, false);
                }
            }
        });

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
            default:
                onClockersClick();
                break;
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    @OnClick(R.id.tab_clockers)
    public void onClockersClick() {
        if (mCurrentBottomTab != CLOCKERS) {
            mCurrentBottomTab = CLOCKERS;
            mTopTabLayout.setVisibility(GONE);
            mTopTabLayout.removeAllTabs();
            replaceFragment(R.id.main_container, ClockersFragment.newInstance(), getString(R.string.clockers));
            mBottomTabLayout[0].setSelected(true);
            mBottomTabLayout[1].setSelected(false);
            mBottomTabLayout[2].setSelected(false);
        }
    }

    @OnClick(R.id.tab_activity)
    public void onActivityClick() {
        if (mCurrentBottomTab != ACTIVITY) {
            mCurrentBottomTab = ACTIVITY;
            replaceFragment(R.id.main_container, EmployeeFragment.newInstance(), getString(R.string.activity));
            mTopTabLayout.setVisibility(VISIBLE);
            mBottomTabLayout[0].setSelected(false);
            mBottomTabLayout[1].setSelected(true);
            mBottomTabLayout[2].setSelected(false);
        }
    }

    @OnClick(R.id.tab_settings)
    public void onSettingsClick() {
        if (mCurrentBottomTab != SETTINGS) {
            mCurrentBottomTab = SETTINGS;
            mTopTabLayout.setVisibility(GONE);
            mTopTabLayout.removeAllTabs();
            replaceFragment(R.id.main_container, SettingsFragment.newInstance(), getString(R.string.settings));
            mBottomTabLayout[0].setSelected(false);
            mBottomTabLayout[1].setSelected(false);
            mBottomTabLayout[2].setSelected(true);
        }
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
//        } else {
//            if (mActivityFragment != null && mActivityFragment.isVisible()) {
//                mActivityFragment.updateToolbar();
//            }
        }
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    public void onFragmentCreated(TopTabListener listener) {
        if (mTopTabLayout != null) {
            listener.setupTabLayout(mTopTabLayout);
        }
    }

    public void changeBottomTabVisibility(boolean focused) {
        mBottomTabContainer.setVisibility((focused) ? GONE : VISIBLE);
    }
}
