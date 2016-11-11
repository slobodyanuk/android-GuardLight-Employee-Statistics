package com.skysoft.slobodyanuk.timekeeper.view.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.skysoft.slobodyanuk.timekeeper.R;

import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodyanuk on 10.11.2016.
 */

public class EmptyRecyclerView extends RecyclerView {

    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && initEmptyView(getContext())) {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean initEmptyView(Context context) {
        return ((emptyView = ButterKnife.findById(getRootView(), R.id.empty)) != null);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(emptyObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }

    public void updateObserver() {
        emptyObserver.onChanged();
    }
}
