package com.skysoft.slobodyanuk.employeestatistics.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.employeestatistics.view.adapter.EmployeeEventAdapter;
import com.skysoft.slobodyanuk.employeestatistics.view.component.SimpleDividerItemDecoration;

import butterknife.BindView;

@SuppressWarnings("SpellCheckingInspection")
public class EventEmployeeFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;

    private EmployeeEventAdapter mAdapter;

    public static EventEmployeeFragment newInstance(int page) {
        EventEmployeeFragment fragment = new EventEmployeeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Globals.PAGE_KEY, page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateToolbar();
        mAdapter = new EmployeeEventAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).unableMenuContainer(R.drawable.ic_nb_charts);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_event;
    }
}
