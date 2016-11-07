package com.skysoft.slobodyanuk.employeestatistics.rest.request;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
