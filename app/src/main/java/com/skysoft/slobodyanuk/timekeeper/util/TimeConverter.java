package com.skysoft.slobodyanuk.timekeeper.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Serhii Slobodyanuk on 09.11.2016.
 */

public class TimeConverter {

    public String getMonth(long timeInMillis){
        Date date = new Date(timeInMillis);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

    public String getTime(long timeInMillis){
        Date date = new Date(timeInMillis);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

}
