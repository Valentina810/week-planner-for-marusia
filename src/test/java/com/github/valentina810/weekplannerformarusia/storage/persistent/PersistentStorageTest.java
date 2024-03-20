package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.MockitoAnnotations.*;

public class PersistentStorageTest {

    private PersistentStorage persistentStorage;

    private static final String EVENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String EVENT_TIME_FOR_STRING = "одиннадцать часов тридцать восемь минут";
    private static final String EVENT_TIME_FOR_TIME = "11:38";
    private static final String EVENT_NAME = "Ужин";

    @BeforeEach
    void setUp() {
        openMocks(this);
        persistentStorage = new PersistentStorage();
    }

    @Test
    void setWeekStorage_whenObjectWithWeek_thenPersistentStorageSetWeekStorage() {
        Week week = Week.builder().build();

        week.addEvent(EVENT_DATE, new Event(EVENT_TIME_FOR_TIME, EVENT_NAME));
        String json = "{\"week\":" + new Gson().toJson(week) + "}";
        persistentStorage.setWeekStorage(json);

        assertNotNull(persistentStorage.getWeekStorage());
        assertEquals(week, persistentStorage.getWeekStorage().getWeek());
    }

    @Test
    void setWeekStorage_whenObjectWithoutWeek_thenNotSetWeekStorage() {
        String json = "{\"key\":\"value\"}";

        persistentStorage.setWeekStorage(json);

        assertNull(persistentStorage.getWeekStorage());
    }

    @Test
    void getEventsByDay_whenExistingDay_thenReturnEvents() {
        Week week = Week.builder().build();
        Event event = new Event(EVENT_TIME_FOR_TIME, EVENT_NAME);
        week.addEvent(EVENT_DATE, event);
        persistentStorage.setWeekStorage(week);

        List<Event> events = persistentStorage.getEventsByDay(EVENT_DATE);

        assertTrue(events.contains(event));
    }

    @Test
    void getEventsByDay_whenNonExistingDay_thenReturnEmptyList() {
        persistentStorage.setWeekStorage(Week.builder().build());

        List<Event> events = persistentStorage.getEventsByDay(EVENT_DATE);

        assertTrue(events.isEmpty());
    }

    @Test
    void getEventsByWeek_whenSetWeekStorage_thenReturnEventsByWeek() {
        Week week = Week.builder().build();
        Event event = new Event(EVENT_TIME_FOR_TIME, EVENT_NAME);
        week.addEvent(EVENT_DATE, event);
        persistentStorage.setWeekStorage(week);

        Map<String, List<Event>> eventsByWeek = persistentStorage.getEventsByWeek();

        assertTrue(eventsByWeek.containsKey(EVENT_DATE));
        assertEquals(event, eventsByWeek.get(EVENT_DATE).get(0));

    }

    @Test
    void setWeekStorage_whenInvalidJson_thenNotSetWeekStorage() {
        String invalidJson = "{invalidJson}";
        assertThrows(JsonSyntaxException.class, () -> persistentStorage.setWeekStorage(invalidJson));
    }

    @Test
    void addEvent_whenWeekStorageNotInitialization_thenInitializeWeekStorageAndAddEvent() {
        PersistentStorage storage = new PersistentStorage();

        String event = storage.addEvent(EVENT_DATE, EVENT_TIME_FOR_STRING, EVENT_NAME);

        assertEquals(EVENT_DATE + " " + EVENT_TIME_FOR_TIME + " " + EVENT_NAME, event);
    }
}