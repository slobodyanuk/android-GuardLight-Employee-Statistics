package com.skysoft.slobodyanuk.timekeeper.data.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Serhii Slobodyanuk on 08.11.2016.
 */

@AllArgsConstructor
@Getter
public class RefreshContentEvent {

    private boolean isConfirm;

}
