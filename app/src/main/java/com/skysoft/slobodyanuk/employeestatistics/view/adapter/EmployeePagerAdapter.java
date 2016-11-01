package com.skysoft.slobodyanuk.employeestatistics.view.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serhii Slobodyanuk on 28.10.2016.
 */

public class EmployeePagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Resources resources;

    public EmployeePagerAdapter(Resources resources, FragmentManager manager) {
        super(manager);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, int title) {
        mFragmentList.add(fragment);
        switch (title) {
            case Globals.TODAY:
                mFragmentTitleList.add(resources.getString(R.string.today));
                break;
            case Globals.WEEK:
                mFragmentTitleList.add(resources.getString(R.string.week));
                break;
            case Globals.MONTH:
                mFragmentTitleList.add(resources.getString(R.string.month));
                break;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
