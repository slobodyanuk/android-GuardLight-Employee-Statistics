package com.skysoft.slobodyanuk.timekeeper.view.activity;

import android.os.Bundle;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.EmployeeInfoFragment;

public class EmployeeInfoActivity extends BaseActivity {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            id = getIntent().getIntExtra(Globals.EMPLOYEE_ID_ARGS, -1);
            replaceFragment(R.id.container, EmployeeInfoFragment.newInstance(id), "info");
        }
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_employee_info;
    }

}
