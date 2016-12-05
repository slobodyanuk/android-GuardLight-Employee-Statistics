package com.skysoft.slobodyanuk.timekeeper.util.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;

/**
 * Created by Serhii Slobodyanuk on 20.10.2016.
 */
public class YAxisValueFormatter implements IAxisValueFormatter {

    private String zeroValue = "0";
    private String hour;
    private String minute;

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value >= 0) {
            hour = String.valueOf((int) (value + Globals.START_WORK_TIME));
            minute = String.valueOf((int) ((value - (int) value) * 60));
            int length = String.valueOf((int) (value + Globals.START_WORK_TIME)).length();

            if (length == 1) {
                hour = String.format("%s%s", zeroValue, hour);
            }

            length = String.valueOf((int) ((value - (int) value) * 60)).length();

            if (length == 1) {
                minute = String.format("%s%s", zeroValue, minute);
            }

            return String.format("%1$s:%2$s", hour, minute);
        } else {
            return "";
        }
    }
}