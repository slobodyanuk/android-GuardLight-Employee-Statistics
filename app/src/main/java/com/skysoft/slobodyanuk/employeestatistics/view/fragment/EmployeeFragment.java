package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.data.Employee;
import com.skysoft.slobodyanuk.employeestatistics.rest.RestClient;
import com.skysoft.slobodyanuk.employeestatistics.service.FragmentEvent;
import com.skysoft.slobodyanuk.employeestatistics.util.PreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class EmployeeFragment extends BaseFragment {

    private static final String TAG = EmployeeFragment.class.getCanonicalName();
    private PreferencesManager mPref = PreferencesManager.getInstance();

    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.text)
    TextView mTextView;

//    private String mMessage = "";

    public static Fragment newInstance() {
        return new EmployeeFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView.setText(mPref.getText());
        if (mScrollView != null) {
            Log.e(TAG, "onViewCreated: " + mScrollView.toString());
            mScrollView.post(() -> {
                if (mScrollView != null) {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
        Observable<List<Employee>> employeeObservable = RestClient.getApiService().getEmployee();
        employeeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employee -> {
                    Log.e(TAG, "onViewCreated: " + employee.get(0).toString());
                }, throwable -> {
                    Toast.makeText(getActivity(), "Failed: " + throwable, Toast.LENGTH_SHORT).show();
                });
    }

    @OnClick(R.id.clear)
    public void onClearLog() {
        mPref.clear();
        mTextView.setText(mPref.getText());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_employee;
    }

    public void showEvent(Map<String, String> data) {

        if (mPref.getText().length() > 40000) {
            mPref.clear();
        }
        mTextView.setText(mPref.getText());
        if (mScrollView != null) {
            mScrollView.post(() -> mScrollView.fullScroll(View.FOCUS_DOWN));
        }

//        if (mTextView != null){
//            mMessage += "\n" +
//                    "Event :: " + data.get(Globals.EVENT_KEY) +
//                    ", time :: " + data.get(Globals.DATE_KEY) +
//                    ", KEY :: " + data.get(Globals.ID_KEY) + "\n";
//            mTextView.setText(mMessage);
//        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FragmentEvent event) {
        showEvent(event.getData());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
