package com.skysoft.slobodyanuk.timekeeper.util;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */
public class IllegalUrlException extends Exception {
    public IllegalUrlException() {
        super("Illegal server address");
    }
}
