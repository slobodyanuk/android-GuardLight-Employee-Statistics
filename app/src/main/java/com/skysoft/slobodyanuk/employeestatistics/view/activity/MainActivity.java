package com.skysoft.slobodyanuk.employeestatistics.view.activity;

import android.os.Bundle;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.view.Navigator;
import com.skysoft.slobodyanuk.employeestatistics.view.fragment.EmployeeFragment;

public class MainActivity extends BaseActivity implements Navigator {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, EmployeeFragment.newInstance())
                .commit();
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_main;
    }

    @Override
    public void navigateToEmployeeStatistics(String id) {

    }
}
