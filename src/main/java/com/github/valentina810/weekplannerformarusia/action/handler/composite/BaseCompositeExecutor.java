package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public interface BaseCompositeExecutor {

    TypeAction getType();

    UnaryOperator<ParametersHandler> getActionExecute();

    /**
     * Выбирает из массива дней все события на определённую дату
     *
     * @param date              - дата
     * @param parametersHandler - набор параметров для работы #todo - рефакторинг - убрать те параметры, которые не нужны
     * @return - ответная фраза со списком событий
     */
    default String getEventsForDate(LocalDate date, ParametersHandler parametersHandler) {
        String defaultMessage = parametersHandler.getLoadCommand().getMessageNegative();
        try {
            parametersHandler.getPersistentStorage().getWeekEvents(parametersHandler.getUserRequest().getState().getUser());
            List<Day> events = parametersHandler.getPersistentStorage().getWeekStorage().getWeek().getDays().stream()
                    .filter(e -> e.getDate().equals(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
                    .filter(a -> !a.getEvents().isEmpty())
                    .toList();
            return events.isEmpty() ? defaultMessage :
                    parametersHandler.getLoadCommand().getMessagePositive() + events.stream()
                            .map(day -> day.getDate() + " " + day.getEvents().stream()
                                    .map(event -> event.getTime() + " " + event.getName())
                                    .collect(Collectors.joining(" ")))
                            .collect(Collectors.joining(" "));
        } catch (NullPointerException e) {
            return defaultMessage;
        }
    }
}