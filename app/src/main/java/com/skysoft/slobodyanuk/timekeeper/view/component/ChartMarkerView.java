package com.skysoft.slobodyanuk.timekeeper.view.component;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.skysoft.slobodyanuk.timekeeper.R;

/**
 * Created by Serhii Slobodyanuk on 25.11.2016.
 */

public class ChartMarkerView extends MarkerView {

    private final TextView tvContent;
    private String content;
    private Highlight highlight;


    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        this.highlight = highlight;
        switch (highlight.getStackIndex()) {
            case 1:
                content = "Clock in time / average\n" + "XX:XX";
                break;
            case 2:
                content = "Time at work / average\n" + "XX:XX";
                break;
            case 3:
                content = "Clock out time / average\n" + "XX:XX";
                break;
        }
        tvContent.setText(content);
    }

//    @Override
//    public int getXOffset(float xpos) {
//        Log.e(TAG, "getXOffset: " + xpos + " ;; "+ highlight.getYPx());
////        int offset = (int) (-getWidth()/2 - (xpos /2));
//        int offset = getWidth() + getWidth() / 2;
//        offset = offset / -2;
////        offset = (int) (offset - xpos /2);
////        if (offset > 0) offset = offset * -1;
//        Log.e("offset", String.valueOf(offset));
//        return xpos;
//    }


}
