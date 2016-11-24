package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;
import com.skysoft.slobodyanuk.timekeeper.util.listener.FragmentListener;
import com.skysoft.slobodyanuk.timekeeper.util.listener.TopTabListener;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.adapter.EmployeePagerAdapter;

import butterknife.BindView;
import io.realm.Realm;

import static com.skysoft.slobodyanuk.timekeeper.util.Globals.EMPLOYEE_ID_ARGS;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.MONTH;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.TODAY;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.WEEK;

/**
 * Created by Serhii Slobodyanuk on 14.11.2016.
 */

public class EmployeeInfoFragment extends BaseFragment implements TopTabListener {

    @BindView(R.id.pager)
    ViewPager mViewPager;

    private Realm mRealm;
    private int id;
    private EmployeePagerAdapter mPagerAdapter;
    private ViewPager.SimpleOnPageChangeListener mPageChangeListener;
    private TabLayout mTabLayout;

    public static EmployeeInfoFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(EMPLOYEE_ID_ARGS, id);
        EmployeeInfoFragment fragment = new EmployeeInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) id = getArguments().getInt(EMPLOYEE_ID_ARGS);
        updateToolbar();
        ((FragmentListener) getActivity()).onFragmentCreated(this);
    }

    private void setupViewPager() {
        mPagerAdapter = new EmployeePagerAdapter(getResources(), getChildFragmentManager());
        mPagerAdapter.addFragment(ChartInfoFragment.newInstance(TODAY, id), TODAY);
        mPagerAdapter.addFragment(ChartInfoFragment.newInstance(WEEK, id), WEEK);
        mPagerAdapter.addFragment(ChartInfoFragment.newInstance(MONTH, id), MONTH);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(setupPageListener());
        mViewPager.post(() -> mPageChangeListener.onPageSelected(0));
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    private ViewPager.SimpleOnPageChangeListener setupPageListener() {
        return mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            int pos = -1;

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment page = mPagerAdapter.getItem(position);
                if (pos != position) {
                    pos = position;
                    ((BaseFragment) page).updateToolbar();
                    ((ChartInfoFragment) page).updateData(pos);
                }
            }
        };
    }

    @Override
    public void updateToolbar() {
        mRealm = Realm.getDefaultInstance();
        ((BaseActivity) getActivity()).disableMenuContainer();
        ((BaseActivity) getActivity()).unableHomeButton();
        ((BaseActivity) getActivity()).unableToolbar();
        ((BaseActivity) getActivity()).setToolbarTitle(mRealm
                .where(Employee.class)
                .equalTo("id", id)
                .findFirst()
                .getName());
        mRealm.close();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_employee;
    }

    @Override
    public void setupTabLayout(TabLayout tabLayout) {
        if (mTabLayout != null) mTabLayout.removeAllTabs();
        mTabLayout = tabLayout;
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.today)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.week)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.month)));
        setupViewPager();
    }
}
