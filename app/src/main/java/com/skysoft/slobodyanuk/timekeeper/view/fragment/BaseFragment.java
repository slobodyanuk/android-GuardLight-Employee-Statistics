package com.skysoft.slobodyanuk.timekeeper.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.view.Navigator;
import com.skysoft.slobodyanuk.timekeeper.view.activity.BaseActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import butterknife.Unbinder;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public abstract class BaseFragment extends RxFragment {

    private Unbinder bind;

    @Nullable
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        bind = ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).disableMenuContainer();
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    public abstract void updateToolbar();

    public abstract int getLayoutResource();

    protected Navigator getNavigator() {
        return (Navigator) getActivity();
    }

}
