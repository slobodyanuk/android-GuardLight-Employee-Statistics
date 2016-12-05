package com.skysoft.slobodyanuk.timekeeper.util.formatter;

import android.graphics.Paint;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.skysoft.slobodyanuk.timekeeper.data.Employee;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Serhii Slobodyanuk on 20.10.2016.
 */
public class NameValueFormatter implements IAxisValueFormatter {

    private boolean isSingleBar;
    private RealmResults<Employee> employees = null;
    private String name = "";

    public NameValueFormatter() {
        Realm mRealm = Realm.getDefaultInstance();
        employees = mRealm.where(Employee.class).findAll();
        this.isSingleBar = false;
        mRealm.close();
    }

    public NameValueFormatter(int id) {
        Realm mRealm = Realm.getDefaultInstance();
        name = mRealm.where(Employee.class).equalTo("id", id).findFirst().getName();
        this.isSingleBar = true;
        mRealm.close();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (!isSingleBar) {
            int iterator = (int) (value);
            try {
                String nameFromRealm = employees.where()
                        .findAll().get(iterator)
                        .getName();

                name = (name.equals(nameFromRealm)) ? "" : nameFromRealm;
                name = nameFromRealm;
                //Label inside bar view
                axis.setXOffset(new Paint().measureText(name));
                return name;
            } catch (Exception e) {
                return "";
            }
        } else {
            // center chart for single user
            if ((int) value == 1) {
                axis.setXOffset((float) (new Paint().measureText(name)));
                return name;
            } else {
                return "";
            }
        }
    }
}