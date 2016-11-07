package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
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
import java.util.TimeZone;

import butterknife.BindView;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class ChartFragment extends BaseFragment implements OnChartValueSelectedListener {
    private static final String TAG = ChartFragment.class.getCanonicalName();

    @BindView(R.id.chart)
    LineChart mLineChart;
    private int[] mColors = new int[]{
            Color.RED,
            Color.GREEN,
    };

    public static Fragment newInstance(int page) {
        ChartFragment fragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Globals.PAGE_KEY, page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateToolbar();
        drawChart();
    }

    private void drawChart() {
        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.setDrawGridBackground(true);
        mLineChart.setDescription("");
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
        xAxis.setAxisMaxValue(32f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one

        xAxis.setValueFormatter(new DaysValueFormatter());

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
        initDataChart();
    }

    private void initDataChart() {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        for (int z = 0; z < 2; z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
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
        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).unableToolbar();
        ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.charts));
        ((BaseActivity) getActivity()).unableChartHomeButton(this);
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
