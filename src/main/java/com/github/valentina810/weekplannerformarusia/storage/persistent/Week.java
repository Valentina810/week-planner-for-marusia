package com.github.valentina810.weekplannerformarusia.storage.persistent;

import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class Week {
    private Map<String, List<Event>> days;//ключ - дата в формате  dd-MM-yyyy

    public Week(Map<String, List<Event>> days) {
        this.days = days;
    }

    public Map<String, List<Event>> getDays() {
        return days == null ? new HashMap<>() : days;
    }

    public void addEvent(String date, Event event) {
        if (days == null) {
            days = new HashMap<>();
        }
        days.compute(date, (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            v.add(event);
            return v;
        });
    }
}