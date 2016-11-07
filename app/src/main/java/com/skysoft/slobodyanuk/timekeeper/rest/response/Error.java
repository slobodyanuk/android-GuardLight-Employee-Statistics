package com.skysoft.slobodyanuk.timekeeper.rest.response;

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
