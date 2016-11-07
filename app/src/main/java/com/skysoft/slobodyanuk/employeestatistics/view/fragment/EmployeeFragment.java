package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.data.event.ChartBackEvent;
import com.skysoft.slobodyanuk.employeestatistics.service.FragmentEvent;
import com.skysoft.slobodyanuk.employeestatistics.util.FragmentListener;
import com.skysoft.slobodyanuk.employeestatistics.util.TopTabListener;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.employeestatistics.view.adapter.EmployeePagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;

import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.MONTH;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.TODAY;
import static com.skysoft.slobodyanuk.employeestatistics.util.Globals.WEEK;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class EmployeeFragment extends BaseFragment implements TopTabListener {

    private static final String TAG = EmployeeFragment.class.getCanonicalName();

    @BindView(R.id.pager)
    ViewPager mViewPager;

    private TabLayout mTabLayout;
    private EmployeePagerAdapter adapter;

    public static Fragment newInstance() {
        Fragment fragment = new EmployeeFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("tag", "onViewCreated: employyee ");

        //updateToolbar();
    }

    private void setupViewPager() {
        adapter = new EmployeePagerAdapter(getResources(), getChildFragmentManager());
        adapter.addFragment(EventEmployeeFragment.newInstance(TODAY), TODAY);
        adapter.addFragment(EventEmployeeFragment.newInstance(WEEK), WEEK);
        adapter.addFragment(EventEmployeeFragment.newInstance(MONTH), MONTH);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupChartViewPager() {
        adapter = new EmployeePagerAdapter(getResources(), getChildFragmentManager());
        adapter.addFragment(ChartFragment.newInstance(TODAY), TODAY);
        adapter.addFragment(ChartFragment.newInstance(WEEK), WEEK);
        adapter.addFragment(ChartFragment.newInstance(MONTH), MONTH);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }

    @Override
    public void setupTabLayout(TabLayout tabLayout) {
        mTabLayout = tabLayout;
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.today)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.week)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.month)));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setupViewPager();
    }

    @Override
    public void updateToolbar() {
        if (isVisible()) {
            Log.e("tag", "onViewCreated: v ");
            ((FragmentListener) getActivity()).onFragmentCreated(this);
            ((BaseActivity) getActivity()).unableToolbar();
            ((BaseActivity) getActivity()).disableHomeButton();
            ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.activity));
            ((BaseActivity) getActivity()).unableMenuContainer(R.drawable.ic_nb_charts)
                    .setOnClickListener(clickedView -> setupChartViewPager());
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_employee;
    }

    public void showEvent(Map<String, String> data) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FragmentEvent event) {
        showEvent(event.getData());
    }

    @Subscribe
    public void onEvent(ChartBackEvent event) {
        setupViewPager();
        updateToolbar();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
