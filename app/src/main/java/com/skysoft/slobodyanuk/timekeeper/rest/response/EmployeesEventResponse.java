package com.skysoft.slobodyanuk.timekeeper.rest.response;

import com.skysoft.slobodyanuk.timekeeper.data.EmployeeEvent;

import java.util.List;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 08.11.2016.
 */
@Getter
public class EmployeesEventResponse extends BaseResponse{

    private List<EmployeeEvent> events;

}
