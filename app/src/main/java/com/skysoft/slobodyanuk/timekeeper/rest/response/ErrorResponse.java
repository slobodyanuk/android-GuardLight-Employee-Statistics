package com.skysoft.slobodyanuk.timekeeper.rest.response;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */

public class ErrorResponse {

    @Getter
    private String message;

    @Getter
    private Error error;
}
