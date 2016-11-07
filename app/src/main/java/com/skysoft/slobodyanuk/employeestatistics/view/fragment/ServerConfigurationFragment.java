package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.util.Globals;
import com.skysoft.slobodyanuk.employeestatistics.util.KeyboardUtil;
import com.skysoft.slobodyanuk.employeestatistics.util.PrefsKeys;
import com.skysoft.slobodyanuk.employeestatistics.util.TypefaceManager;
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
    @BindView(R.id.input_server)
    TextInputLayout mTextInputLayout;

    public static Fragment newInstance() {
        return new ServerConfigurationFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseActivity) getActivity()).disableToolbar();
        mTextInputLayout.setTypeface(TypefaceManager.obtainTypeface(getActivity(), Globals.OPEN_SANS_LIGHT));
        mServerAddress.setOnFocusChangeListener((view1, b) -> {
            if (b) mServerAddress.setText(Prefs.getString(PrefsKeys.SERVER_URL, ""));
        });

    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmServer() {
        Prefs.putBoolean(PrefsKeys.SERVER_AVAILABLE, true);
        String url = String.valueOf(mServerAddress.getText());
        Prefs.putString(PrefsKeys.SERVER_URL, (TextUtils.isEmpty(url) ? Prefs.getString(PrefsKeys.SERVER_URL, "") : url));
        KeyboardUtil.hideKeyboard(getView());
        ((BaseActivity) getActivity())
                .replaceFragment(R.id.container, SignInFragment.newInstance(), getString(R.string.sign_in));
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).disableToolbar();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_server;
    }
}
