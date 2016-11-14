package com.skysoft.slobodyanuk.timekeeper.view.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.data.ClockersItem;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.util.listener.OnEmployeeClickListener;
import com.skysoft.slobodyanuk.timekeeper.view.component.EmptyRecyclerView;
import com.skysoft.slobodyanuk.timekeeper.view.component.TypefaceTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodyanuk on 19.09.2016.
 */
public class EmployeeEventAdapter extends EmptyRecyclerView.Adapter<EmployeeEventAdapter.ListViewHolder> {

    private List<ClockersItem> mItems;
    private Fragment mContext;
    private OnEmployeeClickListener mEmployeeClickListener;

    public EmployeeEventAdapter(Fragment mContext, List<ClockersItem> arrayList) {
        this.mItems = new ArrayList<>(arrayList);
        this.mEmployeeClickListener = (OnEmployeeClickListener) mContext;
        this.mContext = mContext;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_event_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        ClockersItem item = getItem(position);
        holder.root.setOnClickListener(view -> mEmployeeClickListener.onEmployeeClick(item.getId()));
        holder.imgPresent.setImageResource((item.getType().equals(Globals.IN_PASSAGE_KEY)) ? R.drawable.ic_ar_in : R.drawable.ic_ar_out);
        holder.title.setText(item.getName());
        holder.month.setText(item.getMonth());
        holder.time.setText(item.getTime());
    }

    private ClockersItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateData(List<ClockersItem> employeeFromRealm) {
        this.mItems.clear();
        this.mItems = employeeFromRealm;
        this.notifyDataSetChanged();
    }


    public static class ListViewHolder extends EmptyRecyclerView.ViewHolder {

        @BindView(R.id.root)
        RelativeLayout root;
        @BindView(R.id.present)
        ImageView imgPresent;
        @BindView(R.id.title)
        TypefaceTextView title;
        @BindView(R.id.time_month)
        TypefaceTextView month;
        @BindView(R.id.time)
        TypefaceTextView time;

        public ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
