package com.skysoft.slobodyanuk.timekeeper.reactive;

import com.skysoft.slobodyanuk.timekeeper.rest.response.BaseResponse;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public interface OnSubscribeNextListener {

    void onNext(BaseResponse t);

}
