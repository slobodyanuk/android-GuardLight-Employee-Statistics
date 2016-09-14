package com.skysoft.slobodyanuk.employeestatistics.service;

import java.util.Map;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class FragmentEvent {

   private Map<String, String> data;

    public FragmentEvent(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }
}

