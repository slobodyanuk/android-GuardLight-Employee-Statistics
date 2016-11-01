package com.skysoft.slobodyanuk.employeestatistics.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.employeestatistics.view.adapter.ClockersAdapter;
import com.skysoft.slobodyanuk.employeestatistics.view.component.SimpleDividerItemDecoration;

import butterknife.BindView;

@SuppressWarnings("SpellCheckingInspection")
public class ClockersFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    private ClockersAdapter mAdapter;

    public static ClockersFragment newInstance() {
        return new ClockersFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).unableToolbar();
        ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.notification));

        mAdapter = new ClockersAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).unableToolbar();
        ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.notification));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_clockers;
    }
}
