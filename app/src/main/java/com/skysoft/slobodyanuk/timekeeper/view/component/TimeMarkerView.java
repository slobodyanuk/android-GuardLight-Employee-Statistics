package com.skysoft.slobodyanuk.timekeeper.view.component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Created by Sergiy on 28.11.2016.
 */

public class TimeMarkerView extends MarkerView {


    private Highlight highlight;
    private float xOffsetMultiplier;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public TimeMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        this.highlight = highlight;
    }
}
