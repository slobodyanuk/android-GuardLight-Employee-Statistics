package com.skysoft.slobodyanuk.employeestatistics.data;

/**
 * Created by Serhii Slobodyanuk on 31.10.2016.
 */

public class ClockersItem {

    private boolean isPresent;
    private boolean isChecked;
    private boolean isIn;
    private String name;
    private String month;
    private String time;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIn() {
        return isIn;
    }

    public void setIn(boolean in) {
        isIn = in;
    }

}
