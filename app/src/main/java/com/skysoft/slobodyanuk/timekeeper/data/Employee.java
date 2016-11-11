
package com.skysoft.slobodyanuk.timekeeper.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends RealmObject {

    @PrimaryKey
    private int id;

    private String name;

    private boolean isAttendant;

    private boolean isSubscribed;

}
