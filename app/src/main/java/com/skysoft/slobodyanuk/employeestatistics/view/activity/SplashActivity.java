package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.skysoft.slobodyanuk.employeestatistics.R;

import butterknife.BindInt;

public class SplashActivity extends BaseActivity {

    @BindInt(R.integer.splash_duration)
    int mSplashDuration;

    private Handler mHandler;
    private Runnable mStartActivityRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mStartActivityRunnable = () -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        };
        mHandler.postDelayed(mStartActivityRunnable, mSplashDuration);
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mStartActivityRunnable);
    }
}
