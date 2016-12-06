package com.skysoft.slobodyanuk.timekeeper.data.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Sergiy on 06.12.2016.
 */

@AllArgsConstructor
@Getter
public class SubscribeEvent {

    private boolean subscribed;

}
