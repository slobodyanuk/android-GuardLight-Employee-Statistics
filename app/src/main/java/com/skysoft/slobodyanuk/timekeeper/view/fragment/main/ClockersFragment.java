package com.skysoft.slobodyanuk.timekeeper.view.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;
import com.skysoft.slobodyanuk.timekeeper.data.event.RefreshContentEvent;
import com.skysoft.slobodyanuk.timekeeper.data.event.ShowCheckEvent;
import com.skysoft.slobodyanuk.timekeeper.reactive.BaseTask;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.timekeeper.rest.RestClient;
import com.skysoft.slobodyanuk.timekeeper.rest.request.SubscribeEmployeeRequest;
import com.skysoft.slobodyanuk.timekeeper.rest.response.EmployeesResponse;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.IllegalUrlException;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnEmployeeClickListener;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnSubscribeEmployee;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.activity.EmployeeInfoActivity;
import com.skysoft.slobodyanuk.timekeeper.view.adapter.ClockersAdapter;
import com.skysoft.slobodyanuk.timekeeper.view.component.EmptyRecyclerView;
import com.skysoft.slobodyanuk.timekeeper.view.component.SimpleDividerItemDecoration;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Subscription;

@SuppressWarnings("SpellCheckingInspection")
public class ClockersFragment extends BaseFragment
        implements OnSubscribeNextListener, OnSubscribeCompleteListener, SwipeRefreshLayout.OnRefreshListener,
        OnSubscribeEmployee, OnEmployeeClickListener {

    @BindView(R.id.list)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.progress)
    LinearLayout mProgressBar;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;

    private ClockersAdapter mAdapter;
    private List<Employee> employees;
    private Realm mRealm;
    private Subscription mSubscription;
    private int[] mUsers;
    private boolean isChecked = false;

    public static ClockersFragment newInstance() {
        return new ClockersFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRealm = Realm.getDefaultInstance();

        if (mRefreshLayout != null) mRefreshLayout.setOnRefreshListener(this);
        if (mRealm.where(Employee.class).findAll().isEmpty()) {
            executeEmployees();
        } else {
            initRecyclerView(mRealm.where(Employee.class).findAll());
        }

    }

    private void executeEmployees() {
        showProgress();
        try {
            mSubscription = new BaseTask<>().execute(this, RestClient
                    .getApiService()
                    .getEmployees()
                    .compose(bindToLifecycle()));
        } catch (IllegalUrlException e) {
            Toast.makeText(getActivity(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void executeSubscribeEmployees(int[] users) {
        showProgress();
        try {
            mSubscription = new BaseTask<>().execute(this, RestClient
                    .getApiService()
                    .subscribeEmployee(new SubscribeEmployeeRequest(users))
                    .compose(bindToLifecycle()));
        } catch (IllegalUrlException e) {
            Toast.makeText(getActivity(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(true);
        executeEmployees();
    }

    @Override
    public void onCompleted() {
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String ex) {
        hideProgress();
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), ex, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(Object t) {
        if (t instanceof EmployeesResponse) {
            initEmployeesData((EmployeesResponse) t);
            mSubscription.unsubscribe();
        } else {
            mSubscription.unsubscribe();
            executeEmployees();
        }
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
        hideProgress();
    }

    private void initEmployeesData(EmployeesResponse t) {
        employees = new ArrayList<>();
        List<Employee> employeesResponse = t.getEmployees();
        Employee employee;
        for (int i = 0; i < employeesResponse.size(); i++) {
            employee = new Employee();
            employee.setId(employeesResponse.get(i).getId());
            employee.setName(employeesResponse.get(i).getName());
            employee.setAttendant(employeesResponse.get(i).isAttendant());
            employee.setSubscribed(false);
            for (int l = 0; l < t.getSubscribedId().length; l++) {
                if (employeesResponse.get(i).getId() == t.getSubscribedId()[l]) {
                    employee.setSubscribed(true);
                }
            }
            employees.add(employee);
        }
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.where(Employee.class).findAll().deleteAllFromRealm();
        mRealm.insert(employees);
        mRealm.commitTransaction();
        mRealm.close();
        initRecyclerView(employees);
    }

    private void initRecyclerView(List<Employee> employees) {
        mAdapter = new ClockersAdapter(this, employees);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onEvent(RefreshContentEvent ev) {
        isChecked = false;
        ((BaseActivity) getActivity()).disableMenuContainer();
        if (ev.isConfirm()) {
            executeSubscribeEmployees(mUsers);
        } else {
            onRefresh();
        }
    }

    @Override
    public void onSubscribeEmployee(SparseBooleanArray array) {
        mUsers = new int[array.size()];
        for (int i = 0; i < array.size(); i++) {
            mUsers[i] = array.keyAt(i);
        }
        isChecked = true;
        ((BaseActivity) getActivity()).unableMenuContainer(ButterKnife.findById(getActivity(),
                R.id.toolbar_menu_switch));
        SwitchCompat switchCompat = ButterKnife.findById(getActivity(), R.id.menu_switch);
        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> mAdapter.onCheckEmployees(b));
        EventBus.getDefault().post(new ShowCheckEvent());
    }

    @Override
    public void onEmployeeClick(int id) {
        Intent intent = new Intent(getActivity(), EmployeeInfoActivity.class);
        intent.putExtra(Globals.EMPLOYEE_ID_ARGS, id);
        startActivity(intent);
    }

    @Override
    public void updateToolbar() {
        if (isVisible()) {
            ((BaseActivity) getActivity()).disableHomeButton();
            ((BaseActivity) getActivity()).unableToolbar();
            ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.notification));
            if (isChecked) {
                ((BaseActivity) getActivity()).unableMenuContainer(ButterKnife.findById(getActivity(),
                        R.id.toolbar_menu_switch));
            } else {
                ((BaseActivity) getActivity()).disableMenuContainer();
            }
        }
    }

    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_clockers;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
