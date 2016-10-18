package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class ServerConfigurationFragment extends BaseFragment {

    private static final String TAG = ServerConfigurationFragment.class.getCanonicalName();

    @BindView(R.id.et_server_address)
    EditText mServerAddress;

    public static Fragment newInstance() {
        return new ServerConfigurationFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmServer(){
        Prefs.putBoolean(PrefsKeys.SERVER_AVAILABLE_PREF, true);
        ((BaseActivity) getActivity())
                .replaceFragment(R.id.container, SignInFragment.newInstance());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_server;
    }
}
