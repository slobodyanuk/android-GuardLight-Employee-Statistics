
package com.skysoft.slobodyanuk.employeestatistics.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

public class Employee extends RealmObject {

    @Getter
    @PrimaryKey
    @Setter
    private int id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private boolean isAttendant;

    @Getter
    @Setter
    private boolean isSubscribed;

}
