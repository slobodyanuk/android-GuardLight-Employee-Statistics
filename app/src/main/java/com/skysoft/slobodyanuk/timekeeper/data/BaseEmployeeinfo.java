
package com.skysoft.slobodyanuk.timekeeper.data;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEmployeeInfo extends RealmObject {

    private int id;

    private String name;

    private boolean isAttendant;

    private boolean isSubscribed;

}
