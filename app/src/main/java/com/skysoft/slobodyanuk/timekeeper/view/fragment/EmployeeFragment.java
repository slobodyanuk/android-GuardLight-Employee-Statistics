package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.EmployeeEvent;
import com.skysoft.slobodyanuk.timekeeper.data.event.ChartBackEvent;
import com.skysoft.slobodyanuk.timekeeper.reactive.BaseTask;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeCompleteListener;
import com.skysoft.slobodyanuk.timekeeper.reactive.OnSubscribeNextListener;
import com.skysoft.slobodyanuk.timekeeper.rest.RestClient;
import com.skysoft.slobodyanuk.timekeeper.rest.response.EmployeesEventResponse;
import com.skysoft.slobodyanuk.timekeeper.service.FragmentEvent;
import com.skysoft.slobodyanuk.timekeeper.util.IllegalUrlException;
import com.skysoft.slobodyanuk.timekeeper.util.listener.FragmentListener;
import com.skysoft.slobodyanuk.timekeeper.util.listener.TopTabListener;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.skysoft.slobodyanuk.timekeeper.view.adapter.EmployeePagerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import io.realm.Realm;
import rx.Subscription;

import static com.skysoft.slobodyanuk.timekeeper.util.Globals.MONTH;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.TODAY;
import static com.skysoft.slobodyanuk.timekeeper.util.Globals.WEEK;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class EmployeeFragment extends BaseFragment implements TopTabListener,
        OnSubscribeCompleteListener, OnSubscribeNextListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = EmployeeFragment.class.getCanonicalName();

    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.progress)
    LinearLayout mProgressBar;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;

    private TabLayout mTabLayout;
    private EmployeePagerAdapter adapter;
    private boolean created;
    private Realm mRealm;
    private Subscription mSubscription;
    private ViewPager.SimpleOnPageChangeListener mSimpleOnPageChangeListener;

    public static Fragment newInstance() {
        Fragment fragment = new EmployeeFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void executeEmployeeEvent() {
        showProgress();
        try {
            mSubscription = new BaseTask<>().execute(this, RestClient
                    .getApiService()
                    .getEmployeesEvent("month")
                    .compose(bindToLifecycle()));
        } catch (IllegalUrlException e) {
            Toast.makeText(getActivity(), getString(R.string.error_illegal_url), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {
        hideProgress();
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
        hideProgress();
        if (t instanceof EmployeesEventResponse) {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            mRealm.where(EmployeeEvent.class).findAll().deleteAllFromRealm();
            mRealm.copyToRealmOrUpdate(((EmployeesEventResponse) t).getEvents());
            mRealm.commitTransaction();
            mRealm.close();
            ((FragmentListener) getActivity()).onFragmentCreated(this);
        }
    }

    private void setupViewPager() {
        if (adapter == null) {
            adapter = new EmployeePagerAdapter(getResources(), getChildFragmentManager());
            adapter.addFragment(EventEmployeeFragment.newInstance(TODAY), TODAY);
            adapter.addFragment(EventEmployeeFragment.newInstance(WEEK), WEEK);
            adapter.addFragment(EventEmployeeFragment.newInstance(MONTH), MONTH);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(adapter);
            mSimpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    mRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    Log.e("frag", "onPageSelected: twm   " + position + " " + this);
                    if (adapter.getItem(position) instanceof EventEmployeeFragment) {
                        ((EventEmployeeFragment) adapter.getItem(position)).initEventData();
                    }
                }
            };
            mViewPager.addOnPageChangeListener(mSimpleOnPageChangeListener);
            mSimpleOnPageChangeListener.onPageSelected(0);
            mTabLayout.setupWithViewPager(mViewPager, false);
        }else {
            adapter.notifyDataSetChanged();
            mSimpleOnPageChangeListener.onPageSelected(0);
        }
    }

    private void setupChartViewPager() {
        adapter = new EmployeePagerAdapter(getResources(), getChildFragmentManager());
        adapter.addFragment(ChartFragment.newInstance(TODAY), TODAY);
        adapter.addFragment(ChartFragment.newInstance(WEEK), WEEK);
        adapter.addFragment(ChartFragment.newInstance(MONTH), MONTH);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }

    @Override
    public void setupTabLayout(TabLayout tabLayout) {
        if (mTabLayout != null) mTabLayout.removeAllTabs();
        mTabLayout = tabLayout;
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.today)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.week)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.month)));
        setupViewPager();
    }

    @Override
    public void onRefresh() {
        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(true);
        executeEmployeeEvent();
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

    @Override
    public void updateToolbar() {
        if (isVisible()) {
            ((BaseActivity) getActivity()).unableToolbar();
            ((BaseActivity) getActivity()).disableHomeButton();
            ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.activity));
            ((BaseActivity) getActivity()).unableMenuContainer(R.drawable.ic_nb_charts)
                    .setOnClickListener(clickedView -> {
                        setupChartViewPager();
                    });
            executeEmployeeEvent();
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_employee;
    }

    public void showEvent(Map<String, String> data) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FragmentEvent event) {
        showEvent(event.getData());
    }

    @Subscribe
    public void onEvent(ChartBackEvent event) {
        setupViewPager();
        updateToolbar();
    }

    public void updateViewPager() {
//        setupViewPager();
    }

    @Override
    public void onStop() {
        super.onStop();
        created = false;
        EventBus.getDefault().unregister(this);
    }

}
