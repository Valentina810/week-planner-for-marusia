package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.CommandLoader;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public interface BaseCompositeExecutor {
    CommandLoader commandLoader = new CommandLoader();

    TypeAction getType();

    ResponseParameters getResponseParameters(ExecutorParameter executorParameter);

    default Command getCommand() {
        return commandLoader.get(getType());
    }

    /**
     * Выбирает из массива дней все события на определённую дату
     */
    default String getEventsForDate(LocalDate date, Object persistentStorage) {
        Command command = getCommand();
        String defaultMessage = command.getMessageNegative();
        if (defaultMessage != null) {
            PersistentStorage persistentStorage1 = new PersistentStorage();
            persistentStorage1.getWeekEvents(persistentStorage);
            List<Day> eventsByDay = getDays(date, persistentStorage1);
            return getMessage(command, eventsByDay, defaultMessage);
        } else {
            return "";
        }
    }

    private String getMessage(Command command, List<Day> eventsByDay, String defaultMessage) {
        return eventsByDay.isEmpty() ? defaultMessage :
                command.getMessagePositive() + eventsByDay.stream()
                        .map(day -> day.getDate() + " " + day.getEvents().stream()
                                .map(event -> event.getTime() + " " + event.getName())
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(" "));
    }

    private static List<Day> getDays(LocalDate date, PersistentStorage persistentStorage) {
        return persistentStorage
                .getEventsByDay()
                .stream()
                .filter(e -> e.getDate().equals(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
                .filter(a -> !a.getEvents().isEmpty())
                .toList();
    }
}