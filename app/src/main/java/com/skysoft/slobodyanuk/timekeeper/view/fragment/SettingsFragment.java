package com.skysoft.slobodyanuk.timekeeper.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.KeyboardUtil;
import com.skysoft.slobodyanuk.timekeeper.util.PrefsKeys;
import com.skysoft.slobodyanuk.timekeeper.util.TypefaceManager;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.activity.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsFragment extends BaseFragment {

    @BindView(R.id.input_server)
    TextInputLayout mTextInputLayout;
    @BindView(R.id.et_server_address)
    EditText mServerAddress;
    @BindView(R.id.btn_notification)
    Switch mSwitchNotification;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //updateToolbar();
        mTextInputLayout.setTypeface(TypefaceManager.obtainTypeface(getActivity(), Globals.OPEN_SANS_REGULAR));
        mServerAddress.setOnFocusChangeListener((view1, b) -> {
            if (b) mServerAddress.setText(Prefs.getString(PrefsKeys.SERVER_URL, ""));
        });

        mSwitchNotification.setChecked(Prefs.getBoolean(PrefsKeys.NOTIFICATION, true));
        mSwitchNotification.setOnCheckedChangeListener((compoundButton, b) ->
                Prefs.putBoolean(PrefsKeys.NOTIFICATION, b));
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmClick() {
        Prefs.putBoolean(PrefsKeys.SERVER_AVAILABLE, true);
        String url = String.valueOf(mServerAddress.getText());
        Prefs.putString(PrefsKeys.SERVER_URL, (TextUtils.isEmpty(url) ? Prefs.getString(PrefsKeys.SERVER_URL, "") : url));
        KeyboardUtil.hideKeyboard(getView());
    }

    @OnClick(R.id.logout_container)
    public void onLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        builder.setTitle(R.string.logout)
                .setMessage(R.string.logout_text)
                .setCancelable(true)
                .setNegativeButton(R.string.cancel,
                        (dialog, id) -> dialog.cancel())
                .setPositiveButton(android.R.string.ok,
                        (dialog, id) -> logout());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout() {
        Prefs.clear();
        MainActivity.launch(getActivity());
    }

    @Override
    public void updateToolbar() {
        if (isVisible()) {
            ((MainActivity) getActivity()).unableToolbar();
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.settings));
            ((BaseActivity) getActivity()).disableMenuContainer();
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_settings;
    }
}
