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

    public EmptyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (value > 0) {
            return mFormat.format(value);
        } else
            return "";
    }
}
