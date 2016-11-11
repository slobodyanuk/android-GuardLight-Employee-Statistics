package com.skysoft.slobodyanuk.timekeeper.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Serhii Slobodyanuk on 08.11.2016.
 */
@Getter
@Setter
public class EmployeeEvent extends RealmObject{

    @SerializedName("employee_id")
    private int id;

    private String type;

    private long date;

}
