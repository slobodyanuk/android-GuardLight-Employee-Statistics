package com.skysoft.slobodyanuk.timekeeper.rest.request;

import lombok.AllArgsConstructor;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */

@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;

}
