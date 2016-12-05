package com.skysoft.slobodyanuk.timekeeper.util.formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;

import java.text.DecimalFormat;

/**
 * Created by Serhii Slobodyanuk on 24.11.2016.
 */

public class EmptyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    private float lastX = -1;
    private int stackedIndex = -1;
    private Globals.TimeState timeState;

    public EmptyValueFormatter(Globals.TimeState timeState) {
        this.timeState = timeState;
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if (lastX == -1) {
            lastX = entry.getX();
        }
        if (timeState.equals(Globals.TimeState.TODAY)) {
            return "";
        } else {
            if (value > 0 && lastX == entry.getX()) {
                stackedIndex++;
                if (stackedIndex < 3) {
                    if (stackedIndex == 1) {
                        return String.format("%1$30s", mFormat.format(value) + "12:22");
                    } else {
                        return mFormat.format(value) + "12:22";
                    }
                } else {
                    stackedIndex = -1;
                    return "";
                }
            } else {
                lastX = entry.getX();
                stackedIndex = 0;
                return mFormat.format(value) + "12:22";
            }
        }
    }
}
