package com.skysoft.slobodyanuk.timekeeper.util;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.skysoft.slobodyanuk.timekeeper.data.EmployeeInfo;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Serhii Slobodyanuk on 20.10.2016.
 */
public class DataValueFormatter implements ValueFormatter {

    private final RealmResults<EmployeeInfo> employees;
    private String name = "null";

    public DataValueFormatter() {
        Realm mRealm = Realm.getDefaultInstance();
        employees = mRealm.where(EmployeeInfo.class).findAll();
        mRealm.close();
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//        ClockersItem employee = employees.where().equalTo("id", entry.getX()).findFirst();
//        switch (dataSetIndex) {
//            case 1:
//            case 2:
//                return
//            case 3:
//                return
//
//        }
//        employee
        return "10:00";
    }
}