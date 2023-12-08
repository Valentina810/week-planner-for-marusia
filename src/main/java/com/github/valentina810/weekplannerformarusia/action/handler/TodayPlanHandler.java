package com.github.valentina810.weekplannerformarusia.action.handler;

import lombok.Getter;

import java.time.LocalDate;


@Getter
public class TodayPlanHandler extends Handler{

    private final Runnable todayPlan = () -> {
        setDefaultValueParameters();
        respPhrase = getEventsForDate(LocalDate.now());
    };
}