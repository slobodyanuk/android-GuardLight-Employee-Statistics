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

    private RealmResults<Employee> realmResults;
    private boolean isSingleBar;
    private String name = "";
    private float offset;
    private Paint paint = new Paint();

    public NameValueFormatter() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<Employee> employees = mRealm.where(Employee.class).findAll();
        this.isSingleBar = false;
        realmResults = employees.where().findAll();
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
            if (iterator < 0 || iterator >= realmResults.size()) {
                return "";
            } else {
                String realmName = realmResults.get(iterator).getName();

                name = (name.equals(realmName)) ? "" : realmName;
                name = realmName;

                //Label inside bar view]
                if (offset < paint.measureText(name)) {
                    offset = paint.measureText(name);
                    axis.setXOffset(offset);
                }
                return name;
            }
        } else {
            // center chart for single user
            if ((int) value == 1) {
                axis.setXOffset(new Paint().measureText(name));
                return name;
            } else {
                return "";
            }
        }
    }
}