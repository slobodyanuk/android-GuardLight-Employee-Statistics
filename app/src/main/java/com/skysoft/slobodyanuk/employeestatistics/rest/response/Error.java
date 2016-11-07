package com.skysoft.slobodyanuk.employeestatistics.rest.response;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class Error {

    @Getter
    private String message;

    @Getter
    private int status;

    @Getter
    private boolean success;

}
