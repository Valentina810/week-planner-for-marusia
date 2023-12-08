package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Component
public class Handler {
    protected Map<String, Runnable> acts;
    @Setter
    protected BaseLoaderHandler baseLoaderHandler;
    @Setter
    protected UserRequest userRequest;
    protected SessionStorage sessionStorage = new SessionStorage();
    protected PersistentStorage persistentStorage = new PersistentStorage();
    protected String respPhrase;
    protected Boolean isEndSession;

    public Handler() {
        this.acts = new HashMap<>();

        acts.put("WEEKLY_PLAN", new WeeklyPlanHandler().getWeeklyPlan());
        acts.put("TODAY_PLAN", new TodayPlanHandler().getTodayPlan());
        acts.put("TOMORROW_PLAN", new TomorrowPlanHandler().getTomorrowPlan());
//        acts.put("ADD_EVENT", addEventHandler);
    }

    protected String getEventsForDate(LocalDate date) {
        String defaultMessage = baseLoaderHandler.getMessageNegative();
        try {
            persistentStorage.getWeekEvents(userRequest.getState().getUser());
            List<Day> events = persistentStorage.getWeekStorage().getWeek().getDays().stream()
                    .filter(e -> e.getDate().equals(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
                    .filter(a -> !a.getEvents().isEmpty())
                    .toList();
            return events.isEmpty() ? defaultMessage :
                    baseLoaderHandler.getMessagePositive() + events.stream()
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
    protected void setDefaultValueParameters() {
        isEndSession = false;
        this.sessionStorage.getPrevActions(userRequest.getState().getSession());
        this.persistentStorage.getWeekEvents(userRequest.getState().getUser());
    }
}