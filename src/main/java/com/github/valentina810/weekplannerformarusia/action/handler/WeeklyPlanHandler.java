package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WeeklyPlanHandler extends Handler {
    private final Runnable weeklyPlan = () -> {
        setDefaultValueParameters();
        List<Day> collect;
        try {
            collect = persistentStorage.getWeekStorage()
                    .getWeek().getDays().stream()
                    .filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
            if (collect.isEmpty()) {
                respPhrase = baseLoaderHandler.getMessageNegative();
            } else {
                respPhrase = baseLoaderHandler.getMessagePositive() + collect.stream()
                        .map(day -> day.getDate() + " " + day.getEvents().stream()
                                .map(event -> event.getTime() + " " + event.getName())
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(" "));
            }
        } catch (NullPointerException e) {
            respPhrase = baseLoaderHandler.getMessageNegative();
        }
    };
}
