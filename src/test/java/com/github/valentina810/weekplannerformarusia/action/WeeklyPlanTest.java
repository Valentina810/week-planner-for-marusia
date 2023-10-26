package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WeeklyPlanTest {
    private final PersistentStorage persistentStorage = PersistentStorage.builder().build();

    @Test
    public void getWeeklyPlan_whenWeeklyPlanEmpty_thenReturnEmpty() {
        persistentStorage.getWeekEvents();
        BaseAction baseAction = BaseAction.builder()
                .persistentStorage(persistentStorage)
                .build();

        assertEquals("У вас пока нет событий",
                baseAction.replyWeeklyPlan().getMessage());
    }

    @Test
    public void getWeeklyPlan_whenWeeklyPlanContainsOneEvent_thenReturnOneEvent() {
        persistentStorage.setUser_state_update(new Gson().fromJson(FileReader
                .loadJsonFromFile("action/weeklyplan/WeeklyPlanContainsOneEvent.json"), Object.class));
        BaseAction baseAction = BaseAction.builder()
                .persistentStorage(persistentStorage)
                .build();

        assertEquals("Ваши события 26-10-2023 12:00 Лекция",
                baseAction.replyWeeklyPlan().getMessage());
    }

    @Test
    public void getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnOneDay_thenReturnAllEvent() {
        persistentStorage.setUser_state_update(new Gson().fromJson(FileReader
                .loadJsonFromFile("action/weeklyplan/WeeklyPlanContainsTwoEventsOnOneDay.json"), Object.class));
        BaseAction baseAction = BaseAction.builder()
                .persistentStorage(persistentStorage)
                .build();

        assertEquals("Ваши события 26-10-2023 12:00 Лекция 16:00 Свидание",
                baseAction.replyWeeklyPlan().getMessage());
    }

    @Test
    public void getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnDifferentDay_thenReturnAllEvent() {
        persistentStorage.setUser_state_update(new Gson().fromJson(FileReader
                .loadJsonFromFile("action/weeklyplan/WeeklyPlanContainsTwoEventsOnDifferentDay.json"), Object.class));
        BaseAction baseAction = BaseAction.builder()
                .persistentStorage(persistentStorage)
                .build();

        assertEquals("Ваши события 26-10-2023 12:00 Лекция 29-10-2023 18:00 Ужин в ресторане",
                baseAction.replyWeeklyPlan().getMessage());
    }

    @Test
    public void getWeeklyPlan_whenWeeklyPlanContainsTwoEventsTwoDays_thenReturnAllEvent() {
        persistentStorage.setUser_state_update(new Gson().fromJson(FileReader
                .loadJsonFromFile("action/weeklyplan/WeeklyPlanContainsTwoEventsTwoDays.json"), Object.class));
        BaseAction baseAction = BaseAction.builder()
                .persistentStorage(persistentStorage)
                .build();

        assertEquals("Ваши события 26-10-2023 12:00 Лекция 16:00 Свидание 30-10-2023 10:00 Велотренировка 20:00 Поход за покупками",
                baseAction.replyWeeklyPlan().getMessage());
    }
}
