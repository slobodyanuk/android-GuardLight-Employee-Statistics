package com.skysoft.slobodyanuk.timekeeper.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnRefreshEnableListener;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.chart.ChartFragment;

import butterknife.BindView;

public class ChartRangeActivity extends BaseActivity implements OnRefreshEnableListener,
        SwipeRefreshLayout.OnRefreshListener, OnSubscribeCompleteListener, OnSubscribeNextListener {

    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progress)
    LinearLayout mProgressBar;

    private long mStartDate;
    private long mEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

        if (savedInstanceState == null) {
            mStartDate = getIntent().getLongExtra(Globals.START_RANGE_DATE_SELECTED, -1);
            mEndDate = getIntent().getLongExtra(Globals.END_RANGE_DATE_SELECTED, -1);

            /*
            * Remove
            * */

            ChartFragment fragment = ChartFragment.newInstance(Globals.DATE_RANGE, mStartDate, mEndDate);
            fragment.setRefreshEnableListener(this);
            replaceFragment(R.id.container, fragment, "range");

            executeDate();
        } else {
            ChartFragment fragment = (ChartFragment) getSupportFragmentManager().findFragmentByTag("range");
            if (fragment != null) fragment.setRefreshEnableListener(this);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    private void executeDate() {
        showProgress();
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_chart_range;
    }

    @Override
    public void onRefreshEnable(boolean enable) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener((enable) ? this : null);
        }
    }

    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onCompleted() {
        hideProgress();
    }

    @Override
    public void onError(String ex) {
        hideProgress();
        Toast.makeText(this, ex, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(Object t) {
        hideProgress();
        ChartFragment fragment = ChartFragment.newInstance(Globals.DATE_RANGE, mStartDate, mEndDate);
        fragment.setRefreshEnableListener(this);
        replaceFragment(R.id.container, fragment, "range");
    }
}
