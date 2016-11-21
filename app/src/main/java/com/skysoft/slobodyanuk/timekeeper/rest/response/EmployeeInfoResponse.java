package com.skysoft.slobodyanuk.timekeeper.rest.response;

import com.google.gson.annotations.SerializedName;
import com.skysoft.slobodyanuk.timekeeper.data.BaseEmployeeInfo;
import com.skysoft.slobodyanuk.timekeeper.data.RealmString;

import io.realm.RealmList;
import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class EmployeeInfoResponse extends BaseResponse {

    @Getter
    BaseEmployeeInfo employee;

    @Getter
    RealmList<RealmString> arriveTime;

    @Getter
    RealmList<RealmString> leftTime;

    @Getter
    RealmList<RealmString> label;

    @Getter
    String totalTime;

    @Getter
    @SerializedName("avarageArriveTime")
    String averageArriveTime;

    @Getter
    @SerializedName("avarageLeftTime")
    String averageLeftTime;

}
