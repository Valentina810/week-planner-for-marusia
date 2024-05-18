package com.github.valentina810.weekplannerformarusia.storage.persistent;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;

@Builder
@EqualsAndHashCode
@ToString
public class WeekStorage {
    @Getter
    private Week week;

    public void addEvent(String date, Event event) {
        week = Optional.ofNullable(week)
                .orElseGet(() -> Week.builder().build());
        week.addEvent(date, event);
    }

    public void removeObsoleteEvents() {
        if (week != null) {
            week.removeObsoleteEvents();
        }
    }
}