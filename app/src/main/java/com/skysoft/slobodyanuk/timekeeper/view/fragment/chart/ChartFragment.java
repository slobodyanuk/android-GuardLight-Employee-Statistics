package com.skysoft.slobodyanuk.timekeeper.view.fragment.chart;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;
import com.skysoft.slobodyanuk.timekeeper.util.date.TimeConverter;
import com.skysoft.slobodyanuk.timekeeper.util.formatter.EmptyValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.formatter.NameValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.formatter.YAxisValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnRefreshEnableListener;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.component.LockableScrollView;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.skysoft.slobodyanuk.timekeeper.R.id.chart;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.END_RANGE_DATE_SELECTED;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.PAGE_KEY;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.START_RANGE_DATE_SELECTED;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.TimeState;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class ChartFragment extends BaseFragment implements OnChartValueSelectedListener, OnChartGestureListener {
    private static final String TAG = ChartFragment.class.getCanonicalName();

    @BindView(chart)
    HorizontalBarChart mChart;
    @BindView(R.id.root)
    LockableScrollView mScrollView;

    private float oldY;
    private int[] mColors;
    private long mStartDate;
    private long mEndDate;

    private TimeState mTimeState = TimeState.TODAY;
    private OnRefreshEnableListener mRefreshEnableListener;

    public static ChartFragment newInstance(int page) {
        ChartFragment fragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_KEY, page);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ChartFragment newInstance(int page, long start, long end) {
        ChartFragment fragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_KEY, page);
        bundle.putLong(START_RANGE_DATE_SELECTED, start);
        bundle.putLong(END_RANGE_DATE_SELECTED, end);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColors = new int[]{
                getResources().getColor(android.R.color.transparent),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(R.color.colorRed)
        };

        if (getArguments() != null) {
            int page = getArguments().getInt(PAGE_KEY);
            switch (page) {
                case 0:
                    mTimeState = TimeState.TODAY;
                    break;
                case 1:
                    mTimeState = TimeState.WEEK;
                    break;
                case 2:
                    mTimeState = TimeState.MONTH;
                    break;
                case 3:
                    mTimeState = TimeState.DATE_RANGE;
                    mStartDate = getArguments().getLong(START_RANGE_DATE_SELECTED);
                    mEndDate = getArguments().getLong(END_RANGE_DATE_SELECTED);
                    break;
            }
        }
        updateToolbar();
        drawChart();
    }

    private void drawChart() {
        mChart.setDescription(null);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnChartGestureListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xl.setValueFormatter(new NameValueFormatter());

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setGranularity(0.1f);
        yr.setValueFormatter(new YAxisValueFormatter());

        mChart.getAxisLeft().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        initDataChart(mTimeState);

    }

    private void initDataChart(TimeState state) {
        Realm mRealm = Realm.getDefaultInstance();
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        RealmResults<Employee> employees = mRealm.where(Employee.class).findAll();

        XAxis xl = mChart.getXAxis();
        xl.setAxisMaxValue(employees.size());
        xl.setAxisMinValue(-1);
        xl.setGranularity(1f);
        xl.setLabelCount(employees.size());

        if (employees.size() > 5) {
            mChart.zoom(1f, 2f, mChart.getCenterOfView().getX(), mChart.getCenterOfView().getY());
        }
        TimeConverter timeConverter =  new TimeConverter();

        for (int i = 0; i < employees.size(); i++) {
            float[] date = timeConverter.getBarTime("7:43", "8:20", "18:18", "19:05");

            yVals1.add(new BarEntry(i, date));
        }
        mRealm.close();

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Data");
            set1.setColors(mColors);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setDrawValues(true);
            data.setValueFormatter(new EmptyValueFormatter(mTimeState));
            data.setBarWidth(0.4f);
            data.setValueTextColor(Color.WHITE);
            mChart.setData(data);
        }

        mChart.getXAxis().setCenterAxisLabels(true);
        mChart.setFitBars(true);
        mChart.invalidate();
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).unableToolbar();
        if (mTimeState.equals(TimeState.DATE_RANGE)){
            ((BaseActivity) getActivity()).unableHomeButton();
            ((BaseActivity) getActivity()).disableMenuContainer();
            ((BaseActivity) getActivity()).setToolbarTitle(String.format(Locale.getDefault(),
                    "%1$ta, %1$tb  %1$te  -  %2$ta, %2$tb  %2$te", new Date(mStartDate), new Date(mEndDate)));
        }else {
            ((BaseActivity) getActivity()).unableChartHomeButton(this);
            ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.charts));
            ((BaseActivity) getActivity())
                    .unableMenuContainer(R.drawable.ic_nb_calendar)
                    .setOnClickListener(view -> {
                        DialogFragment datePickerFragment = new DatePickerFragment();
                        datePickerFragment.show(getActivity().getFragmentManager(), "Date Picker");
                    });
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_chart;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (h.getStackIndex() == 0) {
            mChart.highlightValues(null);
        }
    }

    public void setRefreshEnableListener(OnRefreshEnableListener listener) {
        this.mRefreshEnableListener = listener;
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        mRefreshEnableListener.onRefreshEnable(true);
        if (mScrollView != null) {
            mScrollView.setScrollingEnabled(true);
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        mRefreshEnableListener.onRefreshEnable(false);
        if (mScrollView != null) {
            mScrollView.setScrollingEnabled(false);
        }
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        if (dY == oldY) {
            mRefreshEnableListener.onRefreshEnable(true);
            if (mScrollView != null) {
                mScrollView.setScrollingEnabled(true);
            }
        }
        oldY = dY;
    }
}
