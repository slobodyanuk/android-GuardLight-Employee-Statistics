package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResources());
        ButterKnife.bind(this);
    }

    public void replaceFragment(int container, Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(container, fragment)
                .commit();
    }

    public void addFragment(int container, Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .add(container, fragment)
                .addToBackStack(String.valueOf(fragment.getId()))
                .commit();
    }


    protected abstract int getLayoutResources();
}
