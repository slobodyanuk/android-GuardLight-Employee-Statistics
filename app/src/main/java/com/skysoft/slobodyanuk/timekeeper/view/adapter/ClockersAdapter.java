package com.skysoft.slobodyanuk.timekeeper.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnEmployeeClickListener;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnSubscribeEmployee;
import com.skysoft.slobodyanuk.timekeeper.view.component.TypefaceTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodyanuk on 19.09.2016.
 */
public class ClockersAdapter extends RecyclerView.Adapter<ClockersAdapter.ListViewHolder> {

    private List<Employee> mItems = new ArrayList<>();
    private static final int mRedCircle = R.drawable.red_circle;
    private static final int mBlueCircle = R.drawable.blue_circle;
    private OnSubscribeEmployee mSubscriber;
    private OnEmployeeClickListener mEmployeeClickListener;
    private boolean[] checked;
    private SparseBooleanArray mSubscribedId = new SparseBooleanArray();

    public ClockersAdapter(Fragment mContext, List<Employee> t) {
        this.mItems = t;
        this.mSubscriber = (OnSubscribeEmployee) mContext;
        this.mEmployeeClickListener = (OnEmployeeClickListener) mContext;
        this.checked = new boolean[mItems.size()];
        initCheckArray();
    }

    private void initCheckArray() {
        for (int i = 0; i < mItems.size(); i++) {
            checked[i] = mItems.get(i).isSubscribed();
            if (checked[i]) {
                mSubscribedId.put(mItems.get(i).getId(), true);
            }
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clockers_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        Employee item = mItems.get(position);
        holder.root.setOnClickListener(view -> mEmployeeClickListener.onEmployeeClick(item.getId()));
        holder.imgPresent.setImageResource((item.isAttendant()) ? mBlueCircle : mRedCircle);
        holder.title.setText(item.getName());
        holder.btnSwitch.setChecked(checked[position]);
        holder.btnSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            checked[position] = b;
            if (mSubscribedId.size() > 0 && mSubscribedId.get(mItems.get(position).getId())) {
                mSubscribedId.delete(mItems.get(position).getId());
            }
            if (b) mSubscribedId.put(mItems.get(position).getId(), true);
            mSubscriber.onSubscribeEmployee(mSubscribedId);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        RelativeLayout root;
        @BindView(R.id.present)
        ImageView imgPresent;
        @BindView(R.id.title)
        TypefaceTextView title;
        @BindView(R.id.btn_switch)
        Switch btnSwitch;

        public ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
