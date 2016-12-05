package com.skysoft.slobodyanuk.timekeeper.view.activity;

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
import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.event.RefreshContentEvent;
import com.skysoft.slobodyanuk.timekeeper.data.event.ShowCheckEvent;
import com.skysoft.slobodyanuk.timekeeper.service.RegistrationIntentService;
import com.skysoft.slobodyanuk.timekeeper.util.KeyboardUtil;
import com.skysoft.slobodyanuk.timekeeper.util.PrefsKeys;
import com.skysoft.slobodyanuk.timekeeper.util.listener.AppBarStateListener;
import com.skysoft.slobodyanuk.timekeeper.util.listener.FragmentListener;
import com.skysoft.slobodyanuk.timekeeper.util.listener.TopTabListener;
import com.skysoft.slobodyanuk.timekeeper.view.Navigator;
import com.skysoft.slobodyanuk.timekeeper.view.adapter.MainPagerAdapter;
import com.skysoft.slobodyanuk.timekeeper.view.component.NonSwipeableViewPager;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.BaseFragment;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.ClockersFragment;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.EmployeeFragment;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.SettingsFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.ACTIVITY;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.CLOCKERS;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.SETTINGS;

public class MainActivity extends BaseActivity
        implements Navigator, FragmentListener {

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
    @BindView(R.id.check_container)
    LinearLayout mCheckContainer;
    @BindView(R.id.bottom_tabs)
    LinearLayout mTabsContainer;

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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mNonSwipeableViewPager.post(() -> mNonSwipeableViewPager.setCurrentItem(ACTIVITY, false));
    }

    private ViewPager.SimpleOnPageChangeListener setupPageListener() {
        return mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            int pos = -1;
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment page = mPagerAdapter.getItem(position);
                mCurrentBottomTab = position;
                initBottomTabs();
                if (pos != position) {
                    pos = position;
                    ((BaseFragment) page).updateToolbar();
                }
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

    @Subscribe
    public void onEvent(ShowCheckEvent ev) {
        showCheckContainer();
    }

    private void showCheckContainer() {
        mCheckContainer.setVisibility(VISIBLE);
        mTabsContainer.setVisibility(GONE);
    }

    private void hideCheckContainer() {
        mCheckContainer.setVisibility(GONE);
        mTabsContainer.setVisibility(VISIBLE);
    }

    @OnClick(R.id.btn_confirm_check)
    public void onConfirmCheck() {
        EventBus.getDefault().post(new RefreshContentEvent(true));
        hideCheckContainer();
    }

    @OnClick(R.id.btn_cancel_check)
    public void onCancelCheck() {
        EventBus.getDefault().post(new RefreshContentEvent(false));
        hideCheckContainer();
    }

    @SuppressWarnings("SpellCheckingInspection")
    @OnClick(R.id.tab_clockers)
    public void onClockersClick() {
        mCurrentBottomTab = CLOCKERS;
        mTopTabLayout.setVisibility(GONE);
        mTopTabLayout.removeAllTabs();
        mNonSwipeableViewPager.setCurrentItem(CLOCKERS, false);
        mBottomTabLayout[0].setSelected(true);
        mBottomTabLayout[1].setSelected(false);
        mBottomTabLayout[2].setSelected(false);
        expandToolbar(false);
    }

    @OnClick(R.id.tab_activity)
    public void onActivityClick() {
        mCurrentBottomTab = ACTIVITY;
        mTopTabLayout.setVisibility(VISIBLE);
        mNonSwipeableViewPager.setCurrentItem(ACTIVITY, false);
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
        mNonSwipeableViewPager.setCurrentItem(SETTINGS, false);
        mBottomTabLayout[0].setSelected(false);
        mBottomTabLayout[1].setSelected(false);
        mBottomTabLayout[2].setSelected(true);
        expandToolbar(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int i = getSupportFragmentManager().getBackStackEntryCount();
        if (i >= 1) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager()
                    .getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            BaseFragment currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(str);
            if (currentFragment != null) {
                currentFragment.updateToolbar();
            }
        }
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    public TabLayout getTopTabLayout() {
        return mTopTabLayout;
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

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
