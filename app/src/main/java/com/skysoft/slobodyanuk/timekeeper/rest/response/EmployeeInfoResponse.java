package com.skysoft.slobodyanuk.timekeeper.rest.response;

import com.google.gson.annotations.SerializedName;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class EmployeeInfoResponse extends BaseResponse {

    @Getter
    Employee employee;

    @Getter
    String[] arriveTime;

    @Getter
    String[] leftTime;

    @Getter
    String[] label;

    @Getter
    String totalTime;

    @Getter
    @SerializedName("avarageArriveTime")
    String averageArriveTime;

    @Getter
    @SerializedName("avarageLeftTime")
    String averageLeftTime;

}
