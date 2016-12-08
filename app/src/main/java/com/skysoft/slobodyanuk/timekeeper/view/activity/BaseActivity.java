package com.skysoft.slobodyanuk.timekeeper.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.event.ChartBackEvent;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.chart.ChartFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @BindView(R.id.bar_layout)
    AppBarLayout mAppBarLayout;

    private Unbinder mBinder;
    private boolean isChartFragment;
    private ChartFragment fragment;
    private RelativeLayout mCurrentMenuContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResources());
        mBinder = ButterKnife.bind(this);
        fragment = null;
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            applyToolbarFont(this);
            mToolbar.setTitleTextColor(Color.WHITE);
        }
        isChartFragment = false;
        disableMenuContainer();
    }

    private void applyToolbarFont(Activity context) {
        for (int i = 0; i < mToolbar.getChildCount(); i++) {
            View view = mToolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                try {
                    Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
                    f.setAccessible(true);
                    tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    tv.setFocusable(true);
                    tv.setFocusableInTouchMode(true);
                    tv.requestFocus();
                    tv.setSingleLine(true);
                    tv.setSelected(true);
                    tv.setMarqueeRepeatLimit(-1);

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                Toolbar.LayoutParams lp = (Toolbar.LayoutParams) tv.getLayoutParams();
                lp.width = Toolbar.LayoutParams.MATCH_PARENT;
                tv.setLayoutParams(lp);
                Typeface titleFont = Typeface.
                        createFromAsset(context.getAssets(), "fonts/OpenSans-Semibold.ttf");
                if (tv.getText().equals(context.getTitle())) {
                    tv.setTypeface(titleFont);
                    break;
                }
            }
        }
    }

    public void unableToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
        }
    }

    public void disableToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void setToolbarTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    public void unableHomeButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void unableChartHomeButton(ChartFragment fragment) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.fragment = fragment;
            isChartFragment = true;
        }
    }

    public void disableHomeButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void replaceFragment(int container, Fragment fragment, String tag) {
        disableHomeButton();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(container, fragment, tag)
                .commit();
    }

    public void addFragment(int container, Fragment fragment, String tag) {
        unableHomeButton();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .add(container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    public RelativeLayout unableMenuContainer(RelativeLayout container) {
        if (container != null) {
            if (mCurrentMenuContainer != null) mCurrentMenuContainer.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
            mCurrentMenuContainer = container;
        }
        return container;
    }

    public RelativeLayout unableMenuContainer() {
        if (mCurrentMenuContainer != null) {
            mCurrentMenuContainer.setVisibility(View.VISIBLE);
        }

        return mCurrentMenuContainer;
    }


    public void disableMenuContainer() {
        if (mCurrentMenuContainer != null) {
            mCurrentMenuContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (isChartFragment) {
                    if (fragment != null) {
                        EventBus.getDefault().post(new ChartBackEvent());
                    }
                } else {
                    onBackPressed();
                }
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        if (isChartFragment) {
            if (fragment != null) {
                EventBus.getDefault().post(new ChartBackEvent());
                isChartFragment = false;
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }

    protected abstract int getLayoutResources();
}
