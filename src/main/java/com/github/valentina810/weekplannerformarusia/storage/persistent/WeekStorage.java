package com.github.valentina810.weekplannerformarusia.storage.persistent;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
public class WeekStorage {
    @Getter
    private Week week;

    public void addEvent(String date, Event event) {
        week = Optional.ofNullable(week)
                .orElseGet(() -> Week.builder().build());
        week.addEvent(date, event);
    }
}