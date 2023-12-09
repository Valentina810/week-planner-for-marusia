package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public interface BaseHandler {

    TypeAction getType();

    void getAction(UserRequest userRequest);

    ParametersHandler getParametersHandler();

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

    /**
     * Установить значения по умолчанию для параметров
     * (случай, когда сессия не завершается и не модифицируются данные в
     * хранилищах)
     */
    default void setDefaultValueParameters(ParametersHandler parameters) {
        parameters.setIsEndSession(false);
        parameters.getSessionStorage().getPrevActions(parameters.getUserRequest().getState().getSession());
        parameters.getPersistentStorage().getWeekEvents(parameters.getUserRequest().getState().getUser());
    }
}