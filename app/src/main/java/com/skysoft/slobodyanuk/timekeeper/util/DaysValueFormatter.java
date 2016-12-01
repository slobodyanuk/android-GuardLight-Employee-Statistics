package com.skysoft.slobodyanuk.timekeeper.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Serhii Slobodyanuk on 20.10.2016.
 */
public class DaysValueFormatter implements IAxisValueFormatter {

    private GregorianCalendar calendar;
    private TimeState state;

    public DaysValueFormatter(TimeState state) {
        this.state = state;
        calendar = new GregorianCalendar(Calendar.getInstance().getTimeZone());
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        switch (state) {
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

    private String initHourValue(float value) {
        int time = (int) (Globals.START_WORK_TIME + value);
        return String.format("%1s:%2$tM", time, calendar);
    }

    private String initDayValue(float value) {
        int days = (int) value;
        if (days == 0) {
            days = 1;
        }
        return String.format("%1$d %2$tb", days, calendar);
    }

    public enum TimeState {
        TODAY, WEEK, MONTH
    }
}