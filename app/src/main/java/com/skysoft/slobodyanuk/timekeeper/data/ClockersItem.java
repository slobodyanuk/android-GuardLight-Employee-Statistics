package com.skysoft.slobodyanuk.timekeeper.data;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Serhii Slobodyanuk on 31.10.2016.
 */

@Getter
@Setter
@SuppressWarnings("SpellCheckingInspection")
public class ClockersItem{

    private int id;

    private String name;

    private String month;

    private String time;

    private String type;

    private long date;

}
