package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Содержит методы-обработчики для команд
 */
@Getter
public class CompositeHandlersActionExecute {

    private final UnaryOperator<ParametersHandler> exit =
            parHandler ->
            {
                parHandler.setIsEndSession(true);
                parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive());
                return parHandler;
            };

    private final UnaryOperator<ParametersHandler> weeklyPlan =
            parHandler ->
            {
                List<Day> collect;
                try {
                    collect = parHandler.getPersistentStorage().getWeekStorage()
                            .getWeek().getDays().stream()
                            .filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
                    if (collect.isEmpty()) {
                        parHandler.setRespPhrase(parHandler.getLoadCommand().getMessageNegative());
                    } else {
                        parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive() + collect.stream()
                                .map(day -> day.getDate() + " " + day.getEvents().stream()
                                        .map(event -> event.getTime() + " " + event.getName())
                                        .collect(Collectors.joining(" ")))
                                .collect(Collectors.joining(" ")));
                    }
                } catch (NullPointerException e) {
                    parHandler.setRespPhrase(parHandler.getLoadCommand().getMessageNegative());
                }
                return parHandler;
            };

    private final UnaryOperator<ParametersHandler> tomorrowPlan =
            parHandler ->
            {
                parHandler.setRespPhrase(getEventsForDate(LocalDate.now().plusDays(1), parHandler));
                return parHandler;
            };

    private final UnaryOperator<ParametersHandler> todayPlan =
            parHandler ->
            {
                parHandler.setRespPhrase(getEventsForDate(LocalDate.now(), parHandler));
                return parHandler;
            };

    private final UnaryOperator<ParametersHandler> help =
            parHandler ->
            {
                parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive());
                parHandler.getSessionStorage().addAction(
                        PrevAction.builder()
                                .number(0)
                                .operation(parHandler.getLoadCommand().getOperation())
                                .valueAction("").build());
                return parHandler;
            };

    private final UnaryOperator<ParametersHandler> exitMainMenu =
            parHandler ->
            {
                parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive());
                parHandler.getSessionStorage().clear();
                return parHandler;
            };

    /**
     * Выбирает из массива дней все события на определённую дату
     * @param date - дата
     * @param parametersHandler - набор параметров для работы #todo - рефакторинг - убрать те параметры, которые не нужны
     * @return - ответная фраза со списком событий
     */
    private String getEventsForDate(LocalDate date, ParametersHandler parametersHandler) {
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