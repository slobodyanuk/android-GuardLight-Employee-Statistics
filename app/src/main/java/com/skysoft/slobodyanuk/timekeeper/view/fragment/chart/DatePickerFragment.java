package com.skysoft.slobodyanuk.timekeeper.view.fragment.chart;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.skysoft.slobodyanuk.timekeeper.R;
import com.skysoft.slobodyanuk.timekeeper.util.Globals;
import com.skysoft.slobodyanuk.timekeeper.view.activity.ChartRangeActivity;

import java.text.DateFormat;
import java.util.Locale;

/**
 * Created by Serhii Slobodyanuk on 02.11.2016.
 */

public class DatePickerFragment extends DialogFragment {

    DateFormat mDateFormatter;
    SublimePicker mSublimePicker;

    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onCancelled() {

            dismiss();
        }

        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker,
                                            SelectedDate selectedDate, int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {

            long startRange = selectedDate.getStartDate().getTimeInMillis();
            long endRange = selectedDate.getEndDate().getTimeInMillis();

            Intent intent = new Intent(getActivity(), ChartRangeActivity.class);
            intent.putExtra(Globals.START_RANGE_DATE_SELECTED, startRange);
            intent.putExtra(Globals.END_RANGE_DATE_SELECTED, endRange);
            getActivity().startActivity(intent);
            dismiss();
        }
    };

    public DatePickerFragment() {
        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mSublimePicker = (SublimePicker) getActivity()
                .getLayoutInflater().inflate(R.layout.sublime_picker, container);

        SublimeOptions options = new SublimeOptions();
        options.setAnimateLayoutChanges(true);
        options.setCanPickDateRange(true);
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);

        mSublimePicker.initializePicker(options, mListener);
        return mSublimePicker;
    }

}
