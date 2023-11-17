package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public interface ReceiverEventsForDate {

    default String getEventsForDate(Object weekEvents, LocalDate date, String dateName) {
        String defaultMessage = "У вас пока нет событий на " + dateName;
        try {
            PersistentStorage persistentStorage = new PersistentStorage();
            persistentStorage.getWeekEvents(weekEvents);
            List<Day> events = persistentStorage.getWeekStorage().getWeek().getDays().stream()
                    .filter(e -> e.getDate().equals(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
                    .filter(a -> !a.getEvents().isEmpty())
                    .toList();
            return events.isEmpty() ? defaultMessage :
                    "Ваши события " + events.stream()
                            .map(day -> day.getDate() + " " + day.getEvents().stream()
                                    .map(event -> event.getTime() + " " + event.getName())
                                    .collect(Collectors.joining(" ")))
                            .collect(Collectors.joining(" "));
        } catch (NullPointerException e) {
            return defaultMessage;
        }
    }
}