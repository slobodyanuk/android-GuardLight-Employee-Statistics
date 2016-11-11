package com.skysoft.slobodyanuk.timekeeper.util;

/**
 * Created by Serhii Slobodyanuk on 09.11.2016.
 */

public class TimeConverter {

    public String getMonth(long timeInMillis){
        return String.format("MM/dd/yyyy", timeInMillis);
    }

}
