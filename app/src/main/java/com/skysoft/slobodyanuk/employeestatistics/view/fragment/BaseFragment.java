package com.skysoft.slobodyanuk.employeestatistics.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skysoft.slobodyanuk.employeestatistics.view.Navigator;
import com.skysoft.slobodyanuk.employeestatistics.view.activity.BaseActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public abstract class BaseFragment extends RxFragment {

    private Unbinder bind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        bind = ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).disableMenuContainer();
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
