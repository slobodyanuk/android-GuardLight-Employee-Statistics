package com.skysoft.slobodyanuk.timekeeper.data;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Serhii Slobodyanuk on 21.11.2016.
 */

public class RealmString extends RealmObject {

    @Getter
    @Setter
    private String value;

}
