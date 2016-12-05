package com.skysoft.slobodyanuk.timekeeper.view.fragment.chart;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.skysoft.slobodyanuk.timekeeper.util.formatter.EmptyValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.IllegalUrlException;
import com.skysoft.slobodyanuk.timekeeper.util.formatter.NameValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.formatter.YAxisValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.util.date.TimeConverter;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.component.LockableScrollView;
import com.skysoft.slobodyanuk.timekeeper.view.component.TypefaceTextView;
import com.skysoft.slobodyanuk.timekeeper.view.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;
import rx.Subscription;

import static com.skysoft.slobodyanuk.timekeeper.R.id.chart;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.EMPLOYEE_ID_ARGS;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.PAGE_KEY;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.TimeState;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class ChartInfoFragment extends BaseFragment
        implements OnChartValueSelectedListener, OnChartGestureListener,
        OnSubscribeCompleteListener, OnSubscribeNextListener, SwipeRefreshLayout.OnRefreshListener {

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

    float oldY;
    private Realm mRealm;
    private int id;
    private int[] mColors;
    private TimeConverter mTimeConverter;
    private TimeState mTimeState = TimeState.TODAY;
    private Subscription mSubscription;
    private int page;
    private YAxis yAxis;
    private XAxis xAxis;
    private EmployeeInfo employeeInfo;
    private int pageArgs;
    private boolean isRefresh = false;

    public static Fragment newInstance(int page, int id) {
        ChartInfoFragment fragment = new ChartInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_KEY, page);
        bundle.putInt(EMPLOYEE_ID_ARGS, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimeConverter = new TimeConverter();
        if (mRefreshLayout != null) mRefreshLayout.setOnRefreshListener(this);
        mColors = new int[]{
                getResources().getColor(android.R.color.transparent),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(R.color.colorRed)
        };
        if (getArguments() != null) {
            pageArgs = getArguments().getInt(PAGE_KEY);
            id = getArguments().getInt(EMPLOYEE_ID_ARGS);
            getEmployeeInfo();
        }
        updateToolbar();
    }

    private void getEmployeeInfo() {
        switch (pageArgs) {
            case 0:
                mTimeState = TimeState.TODAY;
                page = 0;
                executeEmployeeInfo("today");
                break;
            case 1:
                mTimeState = TimeState.WEEK;
                page = 1;
                executeEmployeeInfo("week");
                break;
            case 2:
                mTimeState = TimeState.MONTH;
                page = 2;
                executeEmployeeInfo("month");
                break;
        }
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
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
        mSubscription.unsubscribe();
    }

    @Override
    public void onError(String ex) {
        hideProgress();
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
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
        if (isRefresh) {
            if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
            initDataChart(mTimeState);
            isRefresh = false;
        } else {
            if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
            drawChart();
        }
        initTimeText();
        hideProgress();
    }

    private void initTimeText() {
        mRealm = Realm.getDefaultInstance();
        employeeInfo = mRealm.where(EmployeeInfo.class).equalTo("page", this.page).findFirst();
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
        mChart.setDescription(null);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setOnChartGestureListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
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

    private void initDataChart(TimeState state) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        mRealm = Realm.getDefaultInstance();
        EmployeeInfo employees = mRealm.where(EmployeeInfo.class).findFirst();
        mRealm.close();

        xAxis.setAxisMaxValue(1);
        xAxis.setAxisMinValue(1);
        xAxis.setValueFormatter(new NameValueFormatter(employees.getEmployee().getId()));

        xAxis.setGranularity(1f);
        xAxis.setLabelCount(1);

        TimeConverter timeConverter =  new TimeConverter();
        float[] date = timeConverter.getBarTime("7:12", "8:43", "18:00", "19:00");


        yVals1.add(new BarEntry(1, date));
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
            data.setValueFormatter(new EmptyValueFormatter(mTimeState));
            data.setDrawValues(true);
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
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(this);
        }
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
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(null);
        }
        if (mScrollView != null) {
            mScrollView.setScrollingEnabled(false);
        }
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        if (dY == oldY) {
            if (mRefreshLayout != null) {
                mRefreshLayout.setOnRefreshListener(this);
            }
            if (mScrollView != null) {
                mScrollView.setScrollingEnabled(true);
            }
        }
        oldY = dY;
    }

    public void updateData(int pos) {
        mRealm = Realm.getDefaultInstance();
        switch (pos) {
            case 0:
                mTimeState = TimeState.TODAY;
                page = 0;
                if (mRealm.where(EmployeeInfo.class).equalTo("page", page).findAll().isEmpty()) {
                    executeEmployeeInfo("today");
                } else {
                    hideProgress();
                    drawChart();
                }
                break;
            case 1:
                mTimeState = TimeState.WEEK;
                page = 1;
                if (mRealm.where(EmployeeInfo.class).equalTo("page", page).findAll().isEmpty()) {
                    executeEmployeeInfo("week");
                } else {
                    hideProgress();
                    drawChart();
                }
                break;
            case 2:
                mTimeState = TimeState.MONTH;
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

    @Override
    public void onRefresh() {
        isRefresh = true;
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(true);
        getEmployeeInfo();
    }
}
