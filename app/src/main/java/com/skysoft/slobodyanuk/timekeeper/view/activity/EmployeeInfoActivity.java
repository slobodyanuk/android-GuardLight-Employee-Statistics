package com.skysoft.slobodyanuk.timekeeper.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.listener.FragmentListener;
import com.skysoft.slobodyanuk.timekeeper.util.listener.TopTabListener;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.EmployeeInfoFragment;

import butterknife.BindView;

public class EmployeeInfoActivity extends BaseActivity implements FragmentListener{

    @BindView(R.id.tabs)
    TabLayout mTopTabLayout;

    @BindView(R.id.root)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.bar_layout)
    AppBarLayout mAppBarLayout;

    private int id;
    private boolean toolbarVisible;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            id = getIntent().getIntExtra(Globals.EMPLOYEE_ID_ARGS, -1);
            replaceFragment(R.id.container, EmployeeInfoFragment.newInstance(id), "info");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onFragmentCreated(TopTabListener listener) {
        if (mTopTabLayout != null) {
            listener.setupTabLayout(mTopTabLayout);
        }
    }


    @Override
    protected int getLayoutResources() {
        return R.layout.activity_employee_info;
    }

}
