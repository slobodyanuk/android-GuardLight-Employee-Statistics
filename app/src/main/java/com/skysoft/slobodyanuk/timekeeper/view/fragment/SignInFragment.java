package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.reactive.BaseTask;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.timekeeper.rest.RestClient;
import com.skysoft.slobodyanuk.timekeeper.rest.request.LoginRequest;
import com.skysoft.slobodyanuk.timekeeper.rest.response.LoginResponse;
import com.skysoft.slobodyanuk.timekeeper.service.RegistrationIntentService;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.IllegalUrlException;
import com.skysoft.slobodyanuk.timekeeper.util.KeyboardUtil;
import com.skysoft.slobodyanuk.timekeeper.util.PrefsKeys;
import com.skysoft.slobodyanuk.timekeeper.util.TypefaceManager;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnValidationTextListener;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.activity.MainActivity;
import com.skysoft.slobodyanuk.timekeeper.view.activity.SignInActivity;
import com.skysoft.slobodyanuk.timekeeper.view.component.ValidateEditText;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class SignInFragment extends BaseFragment implements OnSubscribeCompleteListener, OnSubscribeNextListener,
        OnValidationTextListener {

    private static final String TAG = SignInFragment.class.getCanonicalName();

    @BindView(R.id.et_username)
    ValidateEditText mUserName;
    @BindView(R.id.et_password)
    ValidateEditText mPassword;
    @BindViews({R.id.input_username, R.id.input_password})
    TextInputLayout[] mTextInputLayouts;
    @BindView(R.id.btn_confirm)
    AppCompatButton mConfirmButton;

    private Subscription mSubscription;
    private boolean isCorrectPass;
    private boolean isCorrectEmail;

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
        ((BaseActivity) getActivity()).disableToolbar();
        mConfirmButton.setEnabled(false);
        mUserName.setListener(this);
        mPassword.setListener(this);
        mTextInputLayouts[0].setTypeface(TypefaceManager.obtainTypeface(getActivity(), Globals.OPEN_SANS_LIGHT));
        mTextInputLayouts[1].setTypeface(TypefaceManager.obtainTypeface(getActivity(), Globals.OPEN_SANS_LIGHT));

        mUserName.setOnFocusChangeListener((view1, b) -> {
            if (b) mUserName.setText(Prefs.getString(PrefsKeys.USERNAME, ""));
        });
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirmSignIn() {
        if (!TextUtils.isEmpty(String.valueOf(mUserName.getText()))) {
            Prefs.putString(PrefsKeys.USERNAME, String.valueOf(mUserName.getText()));
        }
        try {
            mSubscription = new BaseTask<>().execute(this, RestClient
                    .getApiService()
                    .login(new LoginRequest(Prefs.getString(PrefsKeys.USERNAME, ""), String.valueOf(mPassword.getText())))
                    .compose(bindToLifecycle()));
        } catch (IllegalUrlException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
        }

        KeyboardUtil.hideKeyboard(getView());
    }

    @Override
    public void onNext(Object response) {
        if (response instanceof LoginResponse) {
            if (((SignInActivity) getActivity()).checkPlayServices()) {
                Prefs.putBoolean(PrefsKeys.SIGN_IN, true);
                Prefs.putString(PrefsKeys.API_KEY, ((LoginResponse) response).getToken());
                Intent intent = new Intent(getActivity(), RegistrationIntentService.class);
                getActivity().startService(intent);
                MainActivity.launch(getActivity());
            }
        }
    }

    @Override
    public void onCompleted() {
        mSubscription.unsubscribe();
    }

    @Override
    public void onError(String e) {
        Toast.makeText(getActivity(), e, Toast.LENGTH_SHORT).show();

        /*
        * Remove
        * */
        Prefs.putBoolean(PrefsKeys.SIGN_IN, true);
        Prefs.putString(PrefsKeys.API_KEY, "wdf");
        MainActivity.launch(getActivity());
    }

    @Override
    public void onValidate(EditText editText) {
        if (editText.equals(mPassword)) {
            isCorrectPass = true;
        }
        if (editText.equals(mUserName)) {
            isCorrectEmail = true;
        }
        mConfirmButton.setEnabled((isCorrectEmail && isCorrectPass));
    }

    @Override
    public void onValidateError(EditText editText) {
        if (editText.equals(mPassword)) {
            isCorrectPass = false;
        }
        if (editText.equals(mUserName)) {
            isCorrectEmail = false;
        }
        mConfirmButton.setEnabled(false);
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).disableToolbar();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_signin;
    }

}
