package com.skysoft.slobodyanuk.employeestatistics.rest.response;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class BaseResponse{

    @Getter
    private String message;

    @Getter
    private Error error;
}
