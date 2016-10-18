package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class SignInFragment extends BaseFragment {

    private static final String TAG = SignInFragment.class.getCanonicalName();

    @BindView(R.id.et_username)
    EditText mUserName;
    @BindView(R.id.et_password)
    EditText mPassword;

    public static Fragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmSignIn(){
        Prefs.putBoolean(PrefsKeys.SIGN_IN_PREF, true);
        MainActivity.launch(getActivity());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_signin;
    }
}
