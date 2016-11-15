package com.skysoft.slobodyanuk.timekeeper.util.listener;

import android.widget.EditText;

/**
 * Created by Serhii Slobodyanuk on 15.11.2016.
 */

public interface OnValidationTextListener {

    void onValidate(EditText editText);

    void onValidateError(EditText editText);
}
