package com.github.valentina810.weekplannerformarusia.action.handler;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TomorrowPlanHandler extends Handler {

    private final Runnable tomorrowPlan = () -> {
        setDefaultValueParameters();
        respPhrase = getEventsForDate(LocalDate.now().plusDays(1));
    };
}
