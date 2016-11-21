package com.skysoft.slobodyanuk.timekeeper.data;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Serhii Slobodyanuk on 03.11.2016.
 */
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeInfo extends RealmObject {

    @PrimaryKey
    int page;

    @Getter
    private BaseEmployeeInfo employee;

    @Getter
    private RealmList<RealmString> arriveTime = new RealmList<>();

    @Getter
    private RealmList<RealmString> leftTime = new RealmList<>();

    @Getter
    private RealmList<RealmString> label = new RealmList<>();

    @Getter
    private String totalTime = "";

    @Getter
    @SerializedName("avarageArriveTime")
    private String averageArriveTime = "";

    @Getter
    @SerializedName("avarageLeftTime")
    private String averageLeftTime = "";

}