package com.skysoft.slobodyanuk.timekeeper.util.date;

import com.skysoft.slobodyanuk.timekeeper.data.RealmString;

import java.text.ParseException;
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

    public long getTimeFromString(String timeInString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format.parse(timeInString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
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
