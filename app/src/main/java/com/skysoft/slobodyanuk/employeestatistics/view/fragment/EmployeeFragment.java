package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.service.FragmentEvent;
import com.skysoft.slobodyanuk.employeestatistics.util.PreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class EmployeeFragment extends BaseFragment{

    private static final String TAG = EmployeeFragment.class.getCanonicalName();

    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.text)
    TextView mTextView;

    private String mMessage = "";

    public static Fragment newInstance(){
        return new EmployeeFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView.setText(PreferencesManager.getInstance().getText());
        mScrollView.post(() -> mScrollView.fullScroll(mScrollView.FOCUS_DOWN));
//        Observable<List<Employee>> employeeObservable = RestClient.getApiService().getEmployee();
//        employeeObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Employee>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Employee> employee) {
//                        Log.e(TAG, "onNext: " + employee.get(0).toString());
//                    }
//                });
    }

    @OnClick(R.id.clear)
    public void onClearLog(){
        PreferencesManager.getInstance().clear();
        mTextView.setText(PreferencesManager.getInstance().getText());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_employee;
    }

    public void showEvent(Map<String, String> data) {

        if (PreferencesManager.getInstance().getText().length() > 40000){
            PreferencesManager.getInstance().clear();
        }
        mTextView.setText(PreferencesManager.getInstance().getText());
        mScrollView.post(() -> mScrollView.fullScroll(mScrollView.FOCUS_DOWN));


//        if (mTextView != null){
//            mMessage += "\n" +
//                    "Event :: " + data.get(Globals.EVENT_KEY) +
//                    ", time :: " + data.get(Globals.DATE_KEY) +
//                    ", KEY :: " + data.get(Globals.ID_KEY) + "\n";
//            mTextView.setText(mMessage);
//        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FragmentEvent event){
        showEvent(event.getData());
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
