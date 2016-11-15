package com.skysoft.slobodyanuk.timekeeper.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Patterns;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnValidationTextListener;

import lombok.Setter;

/**
 * Created by Serhii Slobodyanuk on 15.11.2016.
 */

public class ValidateEditText extends TextInputEditText implements TextWatcher {

    private int type;

    @Setter
    private OnValidationTextListener listener;

    public ValidateEditText(Context context) {
        super(context);
        addTextChangedListener(this);
        initTypeface(context, null, 0);
    }

    public ValidateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
        initTypeface(context, attrs, 0);
    }

    public ValidateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(this);
        initTypeface(context, attrs, defStyleAttr);
    }

    private void initTypeface(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.TypeEditText, defStyle, 0);
            if (values != null) {
                type = values.getInt(R.styleable.TypeEditText_typeText, type);
                values.recycle();
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        validateEditText(editable);
    }

    private void validateEditText(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            setError(null);
            validateTypeEditText(s);
        } else {
            setError(getResources().getString(R.string.error_empty));
            listener.onValidateError(this);
        }
    }

    private void validateTypeEditText(Editable editable) {
        switch (type) {
            case Globals.EDITTEXT_EMAIL_TYPE:
                if (!Patterns.EMAIL_ADDRESS.matcher(editable).matches()) {
                    setError(getResources().getString(R.string.error_email));
                    listener.onValidateError(this);
                }else {
                    listener.onValidate(this);
                }
                break;
            case Globals.EDITTEXT_URL_TYPE:
                if (!Patterns.WEB_URL.matcher(editable).matches()) {
                    setError(getResources().getString(R.string.error_url));
                    listener.onValidateError(this);
                }else {
                    listener.onValidate(this);
                }
                break;
            case Globals.EDITTEXT_PASSWORD_TYPE:
                //test
                listener.onValidate(this);
                break;
        }
    }

}
