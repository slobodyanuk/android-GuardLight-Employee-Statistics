package com.skysoft.slobodyanuk.timekeeper.view.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serhii Slobodyanuk on 28.10.2016.
 */

public class EmployeePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Resources resources;

    public EmployeePagerAdapter(Resources resources, FragmentManager manager) {
        super(manager);
        this.mFragmentList.clear();
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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

    public void clearFragments() {
        if (mFragmentList != null) {
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
