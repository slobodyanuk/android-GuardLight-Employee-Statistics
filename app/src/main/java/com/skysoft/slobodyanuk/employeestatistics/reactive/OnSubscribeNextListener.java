package com.skysoft.slobodyanuk.employeestatistics.reactive;

import com.skysoft.slobodyanuk.employeestatistics.rest.response.BaseResponse;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public interface OnSubscribeNextListener {

    void onNext(BaseResponse t);

}
