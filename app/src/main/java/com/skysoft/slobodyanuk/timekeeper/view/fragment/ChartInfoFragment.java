package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.skysoft.slobodyanuk.timekeeper.data.EmployeeInfo;
import com.skysoft.slobodyanuk.timekeeper.reactive.BaseTask;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.timekeeper.rest.RestClient;
import com.skysoft.slobodyanuk.timekeeper.rest.response.EmployeeInfoResponse;
import com.skysoft.slobodyanuk.timekeeper.util.DaysValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.IllegalUrlException;
import com.skysoft.slobodyanuk.timekeeper.util.NameValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.YAxisValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.date.TimeConverter;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.component.LockableScrollView;
import com.skysoft.slobodyanuk.timekeeper.view.component.TypefaceTextView;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;
import rx.Subscription;

import static com.skysoft.slobodyanuk.timekeeper.R.id.chart;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.EMPLOYEE_ID_ARGS;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class ChartInfoFragment extends BaseFragment
        implements OnChartValueSelectedListener, OnChartGestureListener,
        OnSubscribeCompleteListener, OnSubscribeNextListener {

    private static final String TAG = ChartInfoFragment.class.getCanonicalName();

    @BindView(R.id.progress)
    LinearLayout mProgressBar;

    @BindView(chart)
    HorizontalBarChart mChart;

    @BindView(R.id.root)
    LockableScrollView mScrollView;

    @BindView(R.id.clock_in_time)
    TypefaceTextView mTextInTime;
    @BindView(R.id.clock_out_time)
    TypefaceTextView mTextOutTime;
    @BindView(R.id.at_work)
    TypefaceTextView mTextAtWork;

    private Realm mRealm;
    private int id;
    private int[] mColors;
    private TimeConverter mTimeConverter;

    private DaysValueFormatter.TimeState mTimeState = DaysValueFormatter.TimeState.TODAY;
    private Subscription mSubscription;
    private int page;
    private YAxis yAxis;
    private XAxis xAxis;

    public static Fragment newInstance(int page, int id) {
        ChartInfoFragment fragment = new ChartInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Globals.PAGE_KEY, page);
        bundle.putInt(EMPLOYEE_ID_ARGS, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimeConverter = new TimeConverter();
        mColors = new int[]{
                getResources().getColor(android.R.color.transparent),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(R.color.colorRed)
        };
        if (getArguments() != null) {
            int pageArgs = getArguments().getInt(Globals.PAGE_KEY);
            id = getArguments().getInt(Globals.EMPLOYEE_ID_ARGS);
            switch (pageArgs) {
                case 0:
                    mTimeState = DaysValueFormatter.TimeState.TODAY;
                    page = 0;
                    executeEmployeeInfo("today");
                    break;
                case 1:
                    mTimeState = DaysValueFormatter.TimeState.WEEK;
                    page = 1;
                    executeEmployeeInfo("week");
                    break;
                case 2:
                    mTimeState = DaysValueFormatter.TimeState.MONTH;
                    page = 2;
                    executeEmployeeInfo("month");
                    break;
            }
        }
        updateToolbar();
    }

    private void executeEmployeeInfo(String period) {
        showProgress();
        try {
            mSubscription = new BaseTask<>()
                    .execute(this, RestClient
                            .getApiService()
                            .getEmployeeInfo(id, period)
                            .compose(bindToLifecycle()));
        } catch (IllegalUrlException e) {
            Toast.makeText(getActivity(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {
        mSubscription.unsubscribe();
    }

    @Override
    public void onError(String ex) {
        hideProgress();
        Toast.makeText(getActivity(), ex, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(Object t) {
        if (t instanceof EmployeeInfoResponse) {
            EmployeeInfoResponse response = (EmployeeInfoResponse) t;

            EmployeeInfo info = new EmployeeInfo(page, response.getEmployee(),
                    response.getArriveTime(), response.getLeftTime(),
                    response.getLabel(), response.getTotalTime(), response.getAverageArriveTime(),
                    response.getAverageLeftTime());

            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(info);
            mRealm.commitTransaction();
            mRealm.close();
        }
        drawChart();
        initTimeText();
        hideProgress();
    }

    private void initTimeText() {
        mRealm = Realm.getDefaultInstance();
        EmployeeInfo employeeInfo = mRealm.where(EmployeeInfo.class).equalTo("page", this.page).findFirst();
        mTextInTime.setText(mTimeConverter.getTextTime(String.valueOf(employeeInfo.getAverageArriveTime())));
        mTextOutTime.setText(mTimeConverter.getTextTime(String.valueOf(employeeInfo.getAverageLeftTime())));
        mTextAtWork.setText(mTimeConverter.getTextTime(String.valueOf(employeeInfo.getTotalTime())));
        mRealm.close();
    }

    private void showProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }


    private void drawChart() {
        mChart.setDescription("");
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnChartGestureListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(true);

        yAxis = mChart.getAxisRight();
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(true);
        yAxis.setGranularity(0.1f);
        yAxis.setValueFormatter(new YAxisValueFormatter());

        mChart.getAxisLeft().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        initDataChart(mTimeState);

    }

    private void initDataChart(DaysValueFormatter.TimeState state) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        mRealm = Realm.getDefaultInstance();
        EmployeeInfo employees = mRealm.where(EmployeeInfo.class).findFirst();
        mRealm.close();

        xAxis.setAxisMinValue(employees.getEmployee().getId());
        xAxis.setAxisMaxValue(employees.getEmployee().getId());
        xAxis.setValueFormatter(new NameValueFormatter(employees.getEmployee().getId()));
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(1);

        //Remove.
        float start0 = 1f;
        float start1 = 1f;
        float start2 = 10f;
        float start3 = 1f;

        yVals1.add(new BarEntry(employees.getEmployee().getId(), new float[]{start0, start1, start2, start3}));
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
            data.setValueTextColor(Color.WHITE);
            data.setBarWidth(0.4f);
            mChart.setData(data);
        }
        mChart.setFitBars(true);
        mChart.invalidate();
    }

    @Override
    public void updateToolbar() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).unableToolbar();
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
        return R.layout.fragment_chart_info;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (h.getStackIndex() == 0) {
            mChart.highlightValues(null);
        }
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        if (mScrollView != null) {
            mScrollView.setScrollingEnabled(true);
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        if (mScrollView != null) {
            mScrollView.setScrollingEnabled(false);
        }
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
        if (mScrollView != null) {
            mScrollView.setScrollingEnabled(false);
        }
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        if (mScrollView != null) {
            mScrollView.setScrollingEnabled(false);
        }
    }

    public void updateData(int pos) {
        mRealm = Realm.getDefaultInstance();
        switch (pos) {
            case 0:
                mTimeState = DaysValueFormatter.TimeState.TODAY;
                page = 0;
                if (mRealm.where(EmployeeInfo.class).equalTo("page", page).findAll().isEmpty()) {
                    executeEmployeeInfo("today");
                } else {
                    hideProgress();
                    drawChart();
                }
                break;
            case 1:
                mTimeState = DaysValueFormatter.TimeState.WEEK;
                page = 1;
                if (mRealm.where(EmployeeInfo.class).equalTo("page", page).findAll().isEmpty()) {
                    executeEmployeeInfo("week");
                } else {
                    hideProgress();
                    drawChart();
                }
                break;
            case 2:
                mTimeState = DaysValueFormatter.TimeState.MONTH;
                page = 2;
                if (mRealm.where(EmployeeInfo.class).equalTo("page", page).findAll().isEmpty()) {
                    executeEmployeeInfo("month");
                } else {
                    hideProgress();
                    drawChart();
                }
                break;
        }
        mRealm.close();
    }
}
