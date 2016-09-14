package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.service.FragmentEvent;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class EmployeeFragment extends BaseFragment{

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
    public int getLayoutResource() {
        return R.layout.fragment_employee;
    }

    public void showEvent(Map<String, String> data) {
        if (mTextView != null){
            mMessage += "\n" +
                    "Event :: " + data.get(Globals.EVENT_KEY) +
                    ", time :: " + data.get(Globals.DATE_KEY) +
                    ", KEY :: " + data.get(Globals.ID_KEY) + "\n";
            mTextView.setText(mMessage);
        }
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
