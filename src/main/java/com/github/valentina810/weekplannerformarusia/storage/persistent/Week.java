package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.github.valentina810.weekplannerformarusia.util.Formatter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@ToString
@EqualsAndHashCode
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

    public void removeObsoleteEvents() {
        days = days.entrySet().stream()
                .filter(entry -> Formatter.convertStringToDate
                        .apply(entry.getKey()).isAfter(LocalDate.now().minusDays(1)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}