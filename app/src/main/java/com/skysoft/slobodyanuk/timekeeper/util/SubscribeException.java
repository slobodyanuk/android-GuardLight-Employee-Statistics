package com.skysoft.slobodyanuk.timekeeper.util;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */
public class SubscribeException extends Exception {
    public SubscribeException() {
        super("Timeout sending token");
    }
}
