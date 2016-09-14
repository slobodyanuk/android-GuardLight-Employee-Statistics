package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.support.v4.app.Fragment;

import com.skysoft.slobodyanuk.employeestatistics.R;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class EmployeeFragment extends BaseFragment{

    public static Fragment newInstance(){
        return new EmployeeFragment();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_employee;
    }
}
