package com.skysoft.slobodyanuk.timekeeper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.skysoft.slobodyanuk.timekeeper.R;

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

}
