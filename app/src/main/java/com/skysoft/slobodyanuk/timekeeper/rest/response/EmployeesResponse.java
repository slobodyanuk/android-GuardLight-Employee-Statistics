package com.skysoft.slobodyanuk.timekeeper.rest.response;

import com.google.gson.annotations.SerializedName;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;

import java.util.List;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class EmployeesResponse extends BaseResponse {

    @Getter
    List<Employee> employees;

    @Getter
    @SerializedName("subscribed_ids")
    int[] subscribedId;
}
