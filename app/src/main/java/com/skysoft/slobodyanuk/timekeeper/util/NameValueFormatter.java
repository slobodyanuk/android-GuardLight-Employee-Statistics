package com.skysoft.slobodyanuk.timekeeper.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Serhii Slobodyanuk on 20.10.2016.
 */
public class NameValueFormatter implements AxisValueFormatter {

    private RealmResults<Employee> employees = null;
    private String name = "null";
    private String nameFromRealm;

    public NameValueFormatter() {
        Realm mRealm = Realm.getDefaultInstance();
        employees = mRealm.where(Employee.class).findAll();
        mRealm.close();
    }

    public NameValueFormatter(int id) {
        Realm mRealm = Realm.getDefaultInstance();
        employees = mRealm.where(Employee.class).equalTo("id", id).findAll();
        mRealm.close();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int id = (int) (value);
        try {
            nameFromRealm = employees.where().equalTo("id", id).findFirst().getName();
            name = (name.equals(nameFromRealm)) ? "" : nameFromRealm;
            name = nameFromRealm;
            return name;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}