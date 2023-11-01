package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanTodayHandlerTest {
    private final PersistentStorage persistentStorage = PersistentStorage.builder().build();

    @Test
    public void getPlanToday_whenPlanTodayEmpty_thenReturnEmpty() {
        persistentStorage.getWeekEvents();
        Action action = Action.builder()
                .persistentStorage(persistentStorage)
                .message("план на сегодня")
                .build();

        assertEquals("У вас пока нет событий на сегодня",
                action.reply().getMessage());
    }

    @Test
    public void getPlanToday_whenPlanTodayContainsOneEvent_thenReturnOneEvent() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String weeklyPlan = Objects.requireNonNull(FileReader.loadStringFromFile("action/plantoday/TodayPlanEmpty.json"))
                .replace("{todayDate}", today)
                .replace("{todayEvents}", "{\"time\": \"12:00\", \"name\": \"Лекция\"}");
        persistentStorage.setUser_state_update(new Gson().fromJson(weeklyPlan, Object.class));
        Action action = Action.builder()
                .persistentStorage(persistentStorage)
                .message("план на сегодня")
                .build();

        assertEquals("Ваши события " + today + " 12:00 Лекция",
                action.reply().getMessage());
    }

    @Test
    public void getPlanToday_whenPlanTodayContainsTwoEvents_thenReturnAllEvent() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String weeklyPlan = Objects.requireNonNull(FileReader.loadStringFromFile("action/plantoday/TodayPlanEmpty.json"))
                .replace("{todayDate}", today)
                .replace("{todayEvents}", "{\"time\": \"12:00\", \"name\": \"Лекция\"}, {\"time\": \"16:00\", \"name\": \"Прогулка на берегу моря\"}");
        persistentStorage.setUser_state_update(new Gson().fromJson(weeklyPlan, Object.class));
        Action action = Action.builder()
                .persistentStorage(persistentStorage)
                .message("план на сегодня")
                .build();

        assertEquals("Ваши события " + today + " 12:00 Лекция 16:00 Прогулка на берегу моря",
                action.reply().getMessage());
    }
}