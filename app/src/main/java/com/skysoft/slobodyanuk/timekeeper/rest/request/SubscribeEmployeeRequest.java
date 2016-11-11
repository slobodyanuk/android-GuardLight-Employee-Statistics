package com.skysoft.slobodyanuk.timekeeper.rest.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/**
 * Created by Serhii Slobodyanuk on 08.11.2016.
 */
@AllArgsConstructor
public class SubscribeEmployeeRequest {

    @SerializedName("subscribed_ids")
    private int[] subscribedId;

}
