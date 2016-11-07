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

    public DaysValueFormatter() {
        calendar = new GregorianCalendar(Calendar.getInstance().getTimeZone());
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
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