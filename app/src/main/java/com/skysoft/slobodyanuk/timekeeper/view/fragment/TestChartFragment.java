package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.util.DaysValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class TestChartFragment extends BaseFragment implements OnChartValueSelectedListener {
    private static final String TAG = TestChartFragment.class.getCanonicalName();

    @BindView(R.id.chart)
    HorizontalBarChart mLineChart;

    private int[] mColors = new int[]{
            Color.RED,
            Color.GREEN,
    };

    private DaysValueFormatter.TimeState mTimeState = DaysValueFormatter.TimeState.TODAY;

    public static Fragment newInstance(int page) {
        TestChartFragment fragment = new TestChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Globals.PAGE_KEY, page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            int page = getArguments().getInt(Globals.PAGE_KEY);
            switch (page) {
                case 0:
                    mTimeState = DaysValueFormatter.TimeState.TODAY;
                    break;
                case 1:
                    mTimeState = DaysValueFormatter.TimeState.WEEK;
                    break;
                case 2:
                    mTimeState = DaysValueFormatter.TimeState.MONTH;
                    break;
            }
        }
        updateToolbar();
        drawChart();
    }

    private void drawChart() {
        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.setDrawGridBackground(true);
        mLineChart.setDescription(null);
        mLineChart.setDrawBorders(true);
        mLineChart.getAxisLeft().setEnabled(true);

        mLineChart.setDragDecelerationFrictionCoef(0.9f);
        mLineChart.getAxisRight().setDrawAxisLine(false);
        mLineChart.getAxisRight().setDrawGridLines(true);
        mLineChart.getAxisRight().setGridColor(Color.CYAN);
        mLineChart.setHighlightPerDragEnabled(true);
        mLineChart.getXAxis().setDrawAxisLine(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.setTouchEnabled(true);
        mLineChart.animateY(1000);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(false);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setAxisMinValue(1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one

        xAxis.setValueFormatter(new DaysValueFormatter(mTimeState));

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue(10f);
        leftAxis.setCenterAxisLabels(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
        initDataChart(mTimeState);
    }

    private void initDataChart(DaysValueFormatter.TimeState state) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        int count;
        int iterator;
        if (state.equals(DaysValueFormatter.TimeState.TODAY)) {
            count = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
            iterator = 0;
            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setAxisMaxValue(count + 1);
            xAxis.setAxisMinValue(iterator);
        } else if (state.equals(DaysValueFormatter.TimeState.WEEK)) {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            c.clear(Calendar.MINUTE);
            c.clear(Calendar.SECOND);
            c.clear(Calendar.MILLISECOND);
            iterator = c.get(Calendar.DAY_OF_MONTH);
            count = calendar.getActualMaximum(Calendar.DAY_OF_WEEK) + iterator - 1;
            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setAxisMaxValue(count + 1);
            xAxis.setAxisMinValue(iterator);
        } else {
            count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setAxisMaxValue(count + 1);
            iterator = 1;
        }
        for (int z = 0; z < 2; z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = iterator; i <= count; i++) {
                int val = (int) ((Math.random() * 8));
                values.add(new Entry(i + 0.5f, val));
            }
            LineDataSet d = new LineDataSet(values, "DataSet " + (z + 1));
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);

            int color = mColors[z % mColors.length];
            d.setColor(color);
            d.setCircleColor(color);
            d.setHighLightColor(Color.BLUE);
            dataSets.add(d);
        }

        LineData data = new LineData(dataSets);
//        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).unableToolbar();
//        ((BaseActivity) getActivity()).unableChartHomeButton(this);
        ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.charts));
        ((BaseActivity) getActivity()).unableMenuContainer(R.drawable.ic_nb_calendar).setOnClickListener(view -> {
            DialogFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.show(getActivity().getFragmentManager(), "Date Picker");
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_chart;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
