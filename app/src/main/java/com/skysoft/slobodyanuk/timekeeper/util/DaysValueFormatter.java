package com.skysoft.slobodyanuk.timekeeper.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Serhii Slobodyanuk on 20.10.2016.
 */
public class DaysValueFormatter implements AxisValueFormatter {

    private GregorianCalendar calendar;
    private TimeState state;

    public enum TimeState {
        TODAY, WEEK, MONTH
    }

    public DaysValueFormatter(TimeState state) {
        this.state = state;
        calendar = new GregorianCalendar(Calendar.getInstance().getTimeZone());
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        switch (state){
            case TODAY:
                return initHourValue(value);
            case WEEK:
                return initDayValue(value);
            case MONTH:
                return initDayValue(value);
            default:
                return null;
        }
    }

    private String initHourValue(float value){
        int time = (int) value;
        return String.format("%s:00", time);
    }

    private String initDayValue(float value){
        int days = (int) value;
        if (days == 0) {
            days = 1;
        }
        return String.format("%1$d %2$tb", days, calendar);
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}