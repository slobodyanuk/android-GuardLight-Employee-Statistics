package com.skysoft.slobodyanuk.timekeeper.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.ClockersItem;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;
import com.skysoft.slobodyanuk.timekeeper.data.EmployeeEvent;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.TimeConverter;
import com.skysoft.slobodyanuk.timekeeper.util.TimeUtil;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.adapter.EmployeeEventAdapter;
import com.skysoft.slobodyanuk.timekeeper.view.component.EmptyRecyclerView;
import com.skysoft.slobodyanuk.timekeeper.view.component.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;

@SuppressWarnings("SpellCheckingInspection")
public class EventEmployeeFragment extends BaseFragment {

    @BindView(R.id.list_event)
    EmptyRecyclerView mRecyclerView;

    @BindView(R.id.empty)
    LinearLayout mEmptyLayout;

    private Realm mRealm;
    private int mPage;


    public static EventEmployeeFragment newInstance(int page) {
        EventEmployeeFragment fragment = new EventEmployeeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Globals.PAGE_KEY, page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateToolbar();
        mPage = (getArguments() != null) ? getArguments().getInt(Globals.PAGE_KEY) : 0;
        initEventData();
    }

    public void initEventData() {
        switch (mPage) {
            case Globals.TODAY:
                List<EmployeeEvent> eventsFromRealm = getEventsFromRealm(false);
                initRecyclerView(eventsFromRealm);
                break;
            case Globals.WEEK:
                initRecyclerView(getEventsFromRealm(true));
                break;
            case Globals.MONTH:
                mRealm = Realm.getDefaultInstance();
                initRecyclerView(mRealm.where(EmployeeEvent.class).findAll());
                mRealm.close();
                break;
        }
    }

    private void initRecyclerView(List<EmployeeEvent> events) {
        List<ClockersItem> employeeFromRealm = getEmployeeFromRealm(events);
        if (mRecyclerView != null) {
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
            EmployeeEventAdapter adapter = new EmployeeEventAdapter(this, employeeFromRealm);
            mRecyclerView.setEmptyView(mEmptyLayout);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private List<ClockersItem> getEmployeeFromRealm(List<EmployeeEvent> events) {
        mRealm = Realm.getDefaultInstance();
        List<ClockersItem> clockersItems = new ArrayList<>();
        TimeConverter timeConverter = new TimeConverter();
        for (int i = 0; i < events.size(); i++) {
            Employee employee = mRealm.where(Employee.class).equalTo("id", events.get(i).getId()).findFirst();
            ClockersItem item = new ClockersItem();
            item.setId(employee.getId());
            item.setName(employee.getName());
            item.setType(events.get(i).getType());
            item.setMonth(timeConverter.getMonth(events.get(i).getDate()));
            item.setTime(timeConverter.getTime(events.get(i).getDate()));
            clockersItems.add(item);
        }
        mRealm.close();
        return clockersItems;
    }

    private List<EmployeeEvent> getEventsFromRealm(boolean isWeek) {
        mRealm = Realm.getDefaultInstance();
        TimeUtil timeUtil = new TimeUtil();
        RealmResults<EmployeeEvent> events = mRealm.where(EmployeeEvent.class).findAll();
        mRealm.close();
        List<EmployeeEvent> sortedEvents = new ArrayList<>();
        for (EmployeeEvent event : events) {
            if (timeUtil.init(isWeek).inTimeRange(event.getDate() / 1000)) {
                sortedEvents.add(event);
            }
        }
        return sortedEvents;
    }

    @Override
    public void updateToolbar() {
        ((BaseActivity) getActivity()).unableMenuContainer(R.drawable.ic_nb_charts);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_event;
    }

}
