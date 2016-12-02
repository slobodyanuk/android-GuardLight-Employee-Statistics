package com.skysoft.slobodyanuk.timekeeper.util;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Serhii Slobodyanuk on 24.11.2016.
 */

public class EmptyValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    private float lastX = -1;
    private int stackedIndex = -1;

    public EmptyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        if (lastX == -1) {
            lastX = entry.getX();
        }

        if (value > 0 && lastX == entry.getX()) {
            stackedIndex++;
            if (stackedIndex < 3) {
                return mFormat.format(value);
            } else {
                stackedIndex = -1;
                return "";
            }
        } else {
            lastX = entry.getX();
            stackedIndex = 0;
            return mFormat.format(value);
        }
    }
}
