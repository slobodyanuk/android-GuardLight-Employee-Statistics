package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;
import com.skysoft.slobodyanuk.timekeeper.reactive.BaseTask;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.timekeeper.rest.RestClient;
import com.skysoft.slobodyanuk.timekeeper.rest.response.EmployeeInfoResponse;
import com.skysoft.slobodyanuk.timekeeper.util.IllegalUrlException;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;

import java.util.Arrays;

import butterknife.BindView;
import io.realm.Realm;
import rx.Subscription;

import static com.skysoft.slobodyanuk.timekeeper.util.Globals.EMPLOYEE_ID_ARGS;

/**
 * Created by Serhii Slobodyanuk on 14.11.2016.
 */

public class EmployeeInfoFragment extends BaseFragment implements OnSubscribeCompleteListener, OnSubscribeNextListener {

    @BindView(R.id.progress)
    LinearLayout mProgressBar;
    @BindView(R.id.text)
    TextView textView;

    private Realm mRealm;
    private int id;
    private Subscription mSubscription;

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
        executeEmployeeInfo();
    }

    private void executeEmployeeInfo() {
        showProgress();
        try {
            mSubscription = new BaseTask<>()
                    .execute(this, RestClient
                            .getApiService()
                            .getEmployeeInfo(id)
                            .compose(bindToLifecycle()));
        } catch (IllegalUrlException e) {
            Toast.makeText(getActivity(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
    public void onCompleted() {
        mSubscription.unsubscribe();
    }

    @Override
    public void onError(String ex) {
        hideProgress();
        Toast.makeText(getActivity(), ex, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(Object t) {
        if (t instanceof EmployeeInfoResponse) {
            EmployeeInfoResponse response = (EmployeeInfoResponse) t;
            textView.setText(response.getEmployee().getName() + "\n" +
                    Arrays.toString(response.getArriveTime()) + "\n" +
                    response.getTotalTime());
        }
        hideProgress();
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
        return R.layout.fragment_employee_info;
    }
}
