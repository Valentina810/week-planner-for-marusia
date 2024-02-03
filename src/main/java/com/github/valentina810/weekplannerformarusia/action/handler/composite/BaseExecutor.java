package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.CommandLoader;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public interface BaseExecutor {

    TypeAction getType();

    ResponseParameters getResponseParameters(ExecutorParameter executorParameter);

    default Command getCommand(TypeAction typeAction) {
        return CommandLoader.get(typeAction);
    }

    /**
     * Выбирает из массива дней все события на определённую дату
     */
    default String getEventsForDate(Command command, LocalDate date, Object persistentStorage) {
        String defaultMessage = command.getMessageNegative();
        if (defaultMessage != null) {
            PersistentStorage persistentStorage1 = new PersistentStorage();
            persistentStorage1.setWeekStorage(persistentStorage);
            List<Event> eventsByDay = getDaysEvents(date, persistentStorage1);
            return getMessage(command, eventsByDay, defaultMessage);
        } else {
            return "";
        }
    }

    private String getMessage(Command command, List<Event> eventsByDay, String defaultMessage) {
        return eventsByDay.isEmpty() ? defaultMessage :
                command.getMessagePositive() + eventsByDay.stream()
                        .map(event -> event.getName() + " " + event.getTime())
                        .collect(Collectors.joining(" "));
    }

    private static List<Event> getDaysEvents(LocalDate date, PersistentStorage persistentStorage) {
        return persistentStorage
                .getEventsByDay(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))); //#todo паттерн вынести в константу
    }
}