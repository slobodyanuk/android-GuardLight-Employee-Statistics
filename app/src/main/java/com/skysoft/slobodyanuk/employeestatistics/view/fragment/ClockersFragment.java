package com.skysoft.slobodyanuk.employeestatistics.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.data.Employee;
import com.skysoft.slobodyanuk.employeestatistics.reactive.BaseTask;
import com.skysoft.slobodyanuk.employeestatistics.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.employeestatistics.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.employeestatistics.rest.RestClient;
import com.skysoft.slobodyanuk.employeestatistics.rest.response.BaseResponse;
import com.skysoft.slobodyanuk.employeestatistics.rest.response.EmployeesResponse;
import com.skysoft.slobodyanuk.employeestatistics.util.IllegalUrlException;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.employeestatistics.view.adapter.ClockersAdapter;
import com.skysoft.slobodyanuk.employeestatistics.view.component.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;

@SuppressWarnings("SpellCheckingInspection")
public class ClockersFragment extends BaseFragment implements OnSubscribeNextListener, OnSubscribeCompleteListener {

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress)
    LinearLayout mProgressBar;

    private ClockersAdapter mAdapter;
    private List<Employee> employees;
    private Realm mRealm;

    public static ClockersFragment newInstance() {
        return new ClockersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateToolbar();
        showProgress();
        if (mRealm.where(Employee.class).findAll().isEmpty()) {
            try {
                new BaseTask<>().execute(this, RestClient
                        .getApiService()
                        .getEmployees()
                        .compose(bindToLifecycle()));
            } catch (IllegalUrlException e) {
                Toast.makeText(getActivity(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            initRecyclerView(mRealm.where(Employee.class).findAll());
        }

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(String ex) {
        hideProgress();
        Toast.makeText(getActivity(), ex, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(BaseResponse t) {
        if (t instanceof EmployeesResponse) {
            employees = new ArrayList<>();
            List<Employee> employeesResponse = ((EmployeesResponse) t).getEmployees();
            Employee employee;
            for (int i = 0; i < employeesResponse.size(); i++) {
                employee = new Employee();
                employee.setId(employeesResponse.get(i).getId());
                employee.setName(employeesResponse.get(i).getName());
                employee.setAttendant(employeesResponse.get(i).isAttendant());
                employee.setSubscribed(false);
                for (int l = 0; l < ((EmployeesResponse) t).getSubscribedId().length; l++) {
                    if (employeesResponse.get(i).getId() == ((EmployeesResponse) t).getSubscribedId()[l]) {
                        employee.setSubscribed(true);
                    }
                }
                employees.add(employee);
            }
            mRealm.beginTransaction();
            mRealm.copyToRealm(employees);
            mRealm.commitTransaction();
            Log.e("tag", "onNext: " + mRealm.where(Employee.class).findAll());
            initRecyclerView(employees);
        }
    }

    private void initRecyclerView(List<Employee> employees) {
        mAdapter = new ClockersAdapter(this, employees);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        hideProgress();
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).unableToolbar();
        ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.notification));
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
        mRealm.close();
        mRealm = null;
    }
}
