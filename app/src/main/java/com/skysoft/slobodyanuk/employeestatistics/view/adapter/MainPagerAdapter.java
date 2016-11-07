package com.skysoft.slobodyanuk.employeestatistics.view.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serhii Slobodyanuk on 28.10.2016.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Resources resources;

    public MainPagerAdapter(Resources resources, FragmentManager manager) {
        super(manager);
        this.mFragmentList.clear();
        this.mFragmentTitleList.clear();
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

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void clearFragments() {
        if (mFragmentList != null) {
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }
        notifyDataSetChanged();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
