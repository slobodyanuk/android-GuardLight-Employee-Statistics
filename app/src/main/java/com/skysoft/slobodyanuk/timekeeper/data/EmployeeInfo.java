package com.skysoft.slobodyanuk.timekeeper.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeeInfo extends RealmObject {

    @PrimaryKey
    int page;

    @Getter
    private BaseEmployeeInfo employee;

    @Getter
    private RealmList<RealmString> arriveTime;

    @Getter
    private RealmList<RealmString> leftTime;

    @Getter
    private RealmList<RealmString> label;

    @Getter
    private String totalTime;

    @Getter
    @SerializedName("avarageArriveTime")
    private String averageArriveTime;

    @Getter
    @SerializedName("avarageLeftTime")
    private String averageLeftTime;

}