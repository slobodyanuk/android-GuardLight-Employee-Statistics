package com.skysoft.slobodyanuk.employeestatistics.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.skysoft.slobodyanuk.employeestatistics.R;
import com.skysoft.slobodyanuk.employeestatistics.data.ClockersItem;
import com.skysoft.slobodyanuk.employeestatistics.view.component.TypefaceTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodyanuk on 19.09.2016.
 */
public class ClockersAdapter extends RecyclerView.Adapter<ClockersAdapter.ListViewHolder> {

    private static ArrayList<ClockersItem> mItems = new ArrayList<>();
    private static final int mRedCircle = R.drawable.red_circle;
    private static final int mBlueCircle = R.drawable.blue_circle;
    private Fragment mContext;


    public ClockersAdapter(Fragment mContext) {
        mItems.clear();
        for (int i = 0; i < 20; i++) {
            ClockersItem item = new ClockersItem();
            item.setName("Item :: " + i);
            mItems.add(item);
        }
        this.mContext = mContext;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clockers_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        ClockersItem item = getItem(position);
        holder.imgPresent.setImageResource((item.isPresent()) ? mRedCircle : mBlueCircle);
        holder.title.setText(item.getName());
        holder.btnSwitch.setChecked(item.isChecked());
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


    public static class ListViewHolder extends RecyclerView.ViewHolder {

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
