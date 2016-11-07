package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.service.RegistrationIntentService;
import com.skysoft.slobodyanuk.employeestatistics.util.AppBarStateListener;
import com.skysoft.slobodyanuk.employeestatistics.util.FragmentListener;
import com.skysoft.slobodyanuk.employeestatistics.util.KeyboardUtil;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;
import com.skysoft.slobodyanuk.employeestatistics.util.TopTabListener;
import com.skysoft.slobodyanuk.employeestatistics.view.Navigator;
import com.skysoft.slobodyanuk.employeestatistics.view.adapter.MainPagerAdapter;
import com.skysoft.slobodyanuk.employeestatistics.view.component.NonSwipeableViewPager;
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
    @SuppressWarnings("SpellCheckingInspection")
    @BindView(R.id.main_pager)
    NonSwipeableViewPager mNonSwipeableViewPager;
    @BindView(R.id.bottom_tab_container)
    FrameLayout mBottomTabContainer;
    @BindView(R.id.root)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.bar_layout)
    AppBarLayout mAppBarLayout;

    @BindViews({R.id.tab_clockers, R.id.tab_activity, R.id.tab_settings})
    LinearLayout[] mBottomTabLayout;

    private FragmentManager mFragmentManager;
    private int mCurrentBottomTab = ACTIVITY;
    private int bottomVisible = VISIBLE;
    private boolean toolbarVisible;
    private MainPagerAdapter mPagerAdapter;
    private ViewPager.SimpleOnPageChangeListener mPageChangeListener;

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
            if (Prefs.getBoolean(PrefsKeys.SIGN_IN, false) &&
                    !TextUtils.isEmpty(Prefs.getString(PrefsKeys.API_KEY, ""))) {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            } else {
                Toast.makeText(this, getString(R.string.sign_in), Toast.LENGTH_SHORT).show();
                SignInActivity.launch(this);
            }
        }
        setupViewPager();
        setupCoordinatorLayout();
    }

    private void setupViewPager() {
            mPagerAdapter = new MainPagerAdapter(getResources(), getSupportFragmentManager());
            mPagerAdapter.addFragment(ClockersFragment.newInstance(), getString(R.string.clockers));
            mPagerAdapter.addFragment(EmployeeFragment.newInstance(), getString(R.string.activity));
            mPagerAdapter.addFragment(SettingsFragment.newInstance(), getString(R.string.settings));
            mNonSwipeableViewPager.setAdapter(mPagerAdapter);
            mNonSwipeableViewPager.addOnPageChangeListener(setupPageListener());
            mNonSwipeableViewPager.post(() -> mPageChangeListener.onPageSelected(ACTIVITY));
    }

    private ViewPager.SimpleOnPageChangeListener setupPageListener() {
        return mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment page = mPagerAdapter.getItem(position);
                ((BaseFragment) page).updateToolbar();
                mCurrentBottomTab = position;
                initBottomTabs();
            }
        };
    }

    private void setupCoordinatorLayout() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, AppBarStateListener.State state) {
                mCoordinatorLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    int heightDiff = mCoordinatorLayout.getRootView().getHeight() - mCoordinatorLayout.getHeight();
                    if (heightDiff > KeyboardUtil.dpToPx(MainActivity.this, 200)) {
                        if (bottomVisible == VISIBLE) {
                            bottomVisible = changeBottomTabVisibility(true);
                            toolbarVisible = false;
                        }
                    } else {
                        if (bottomVisible == GONE) {
                            bottomVisible = changeBottomTabVisibility(false);
                        }
                        expandToolbar(toolbarVisible);
                    }
                });
            }
        });
    }

    private void expandToolbar(boolean toolbarVisible) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            if (bottomVisible == VISIBLE && !toolbarVisible) {
                Handler handler = new Handler();
                handler.post(() -> behavior.onNestedFling(mCoordinatorLayout, mAppBarLayout, null, 0, -10000, false));
                this.toolbarVisible = true;
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
            default:
                onActivityClick();
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @SuppressWarnings("SpellCheckingInspection")
    @OnClick(R.id.tab_clockers)
    public void onClockersClick() {
            mCurrentBottomTab = CLOCKERS;
            mTopTabLayout.setVisibility(GONE);
            mTopTabLayout.removeAllTabs();
            mNonSwipeableViewPager.setCurrentItem(CLOCKERS, true);
            mBottomTabLayout[0].setSelected(true);
            mBottomTabLayout[1].setSelected(false);
            mBottomTabLayout[2].setSelected(false);
            expandToolbar(false);
    }

    @OnClick(R.id.tab_activity)
    public void onActivityClick() {
            mCurrentBottomTab = ACTIVITY;
            mNonSwipeableViewPager.setCurrentItem(ACTIVITY, true);
            mTopTabLayout.setVisibility(VISIBLE);
            mBottomTabLayout[0].setSelected(false);
            mBottomTabLayout[1].setSelected(true);
            mBottomTabLayout[2].setSelected(false);
            expandToolbar(false);
    }

    @OnClick(R.id.tab_settings)
    public void onSettingsClick() {
            mCurrentBottomTab = SETTINGS;
            mTopTabLayout.setVisibility(GONE);
            mTopTabLayout.removeAllTabs();
            mNonSwipeableViewPager.setCurrentItem(SETTINGS, true);
            mBottomTabLayout[0].setSelected(false);
            mBottomTabLayout[1].setSelected(false);
            mBottomTabLayout[2].setSelected(true);
            expandToolbar(false);
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

    public int changeBottomTabVisibility(boolean focused) {
        mBottomTabContainer.setVisibility((focused) ? GONE : VISIBLE);
        return mBottomTabContainer.getVisibility();
    }
}
