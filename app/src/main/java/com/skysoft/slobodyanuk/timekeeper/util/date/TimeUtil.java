package com.skysoft.slobodyanuk.timekeeper.util.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 08.11.2016.
 */

public class TimeUtil {

    @Getter
    private long startTime;

    @Getter
    private long endTime;

    private TimeZone zone;

    public TimeUtil init(boolean isWeek){
        zone = TimeZone.getTimeZone("GMT");
        this.startTime = (!isWeek) ? getStartOfDay() : getStartOfWeek();
        this.endTime = (!isWeek) ? getEndOfDay() : getEndOfWeek();
        return this;
    }

    private long getStartOfDay() {
        Calendar calendar = Calendar.getInstance(zone, Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    private long getEndOfDay() {
        Calendar calendar = Calendar.getInstance(zone, Locale.getDefault());
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis() / 1000;
    }

    private long getStartOfWeek() {
        Calendar calendar = Calendar.getInstance(zone, Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTimeInMillis() / 1000;
    }

    private long getEndOfWeek() {
        Calendar calendar = Calendar.getInstance(zone, Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.add(Calendar.DATE, Calendar.SATURDAY - Calendar.MONDAY);
        return calendar.getTimeInMillis() / 1000;
    }

    public boolean inTimeRange(long time) {

        Calendar calendar1 = Calendar.getInstance(zone, Locale.getDefault());
        calendar1.setTimeInMillis(startTime);
        Date date1 = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance(zone, Locale.getDefault());
        calendar2.setTimeInMillis(endTime);
        Date date2 = calendar2.getTime();


        Calendar calendar3 = Calendar.getInstance(zone, Locale.getDefault());
        calendar3.setTimeInMillis(time);
        Date date3 = calendar3.getTime();

        return date3.after(date1) && date3.before(date2);
    }

}
