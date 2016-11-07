package com.skysoft.slobodyanuk.timekeeper.rest.response;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class TokenResponse {

    private String response;

    public TokenResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
