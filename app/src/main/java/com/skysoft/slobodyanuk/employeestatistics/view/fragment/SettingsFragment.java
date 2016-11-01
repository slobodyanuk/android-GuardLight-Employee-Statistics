package com.skysoft.slobodyanuk.employeestatistics.view.fragment;


import com.skysoft.slobodyanuk.employeestatistics.R;

public class SettingsFragment extends BaseFragment {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }


    @Override
    public void updateToolbar() {

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_settings;
    }
}
