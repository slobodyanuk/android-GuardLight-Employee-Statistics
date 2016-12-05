package com.skysoft.slobodyanuk.timekeeper.util.date;

import com.skysoft.slobodyanuk.timekeeper.data.RealmString;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;

import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import io.realm.RealmList;

import static com.skysoft.slobodyanuk.timekeeper.util.Globals.UNDEFINED;

/**
 * Created by Serhii Slobodyanuk on 09.11.2016.
 */

public class TimeConverter {

    public String getMonthFromMillis(long timeInMillis) {
        Date date = new Date(timeInMillis);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

    public String getTimeFromMillis(long timeInMillis) {
        Date date = new Date(timeInMillis);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

    public float[] getBarTime(String timeIn, String startTimeAtWork, String endTimeAtWork, String timeOut) {
        float[] times = new float[4];
        times[0] = convertTimeString(timeIn) - Globals.START_WORK_TIME;
        times[1] = convertTimeString(startTimeAtWork) - convertTimeString(timeIn);
        times[2] = convertTimeString(endTimeAtWork) - convertTimeString(startTimeAtWork);
        times[3] = convertTimeString(timeOut) - convertTimeString(endTimeAtWork);
        return times;
    }

    private float convertTimeString(String time){
        String[] splitted = time.split(":");
        return Float.parseFloat(splitted[0]) + (Float.parseFloat(splitted[1]) / TimeUtil.HOUR);
    }


    public String getTextTime(String time) {
        if (time.isEmpty() || time.equals("null")) {
            return UNDEFINED;
        } else {
            return time;
        }
    }

    public String getMaxTime(RealmList<RealmString> times) {
        try {
            return ((RealmString) Collections.max((AbstractCollection) times)).getValue();
        } catch (NoSuchElementException ex) {
            return UNDEFINED;
        }
    }

}
