package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Handlers {
    @Getter
    private final Map<TypeAction, BaseHandlerResponsePhrase> baseHandlers;

    public Handlers() {
        WeeklyPlanHandler weeklyPlanHandler = new WeeklyPlanHandler();
        PlanTodayHandler planTodayHandler = new PlanTodayHandler();

        baseHandlers = new HashMap<>();
        baseHandlers.put(weeklyPlanHandler.getType(), weeklyPlanHandler);
        baseHandlers.put(planTodayHandler.getType(), planTodayHandler);
    }
}