package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersistentStorageTest {

    private PersistentStorage persistentStorage;

    private Map<String, List<Event>> days;
    private Week week;
    private static final String EVENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String EVENT_TIME_FOR_STRING = "одиннадцать часов тридцать восемь минут";
    private static final String EVENT_TIME_FOR_TIME = "11:38";
    private static final String EVENT_NAME = "Ужин";

    private static final Event event = Event.builder().time(EVENT_TIME_FOR_TIME)
            .name(EVENT_NAME)
            .build();

    @BeforeAll
    void allSetup() {
        days = new HashMap<>();
        days.put(EVENT_DATE, List.of(event));
        week = new Week(days);
    }

    @BeforeEach
    void setUp() {
        persistentStorage = new PersistentStorage();
    }

    @Test
    void setWeekStorage_shouldUpdateWeekStorage_thenProvidedWithValidWeekObject() {
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
        persistentStorage.setWeekStorage(week);

        List<Event> events = persistentStorage.getEventsByDay(EVENT_DATE);

        assertTrue(events.contains(event));
    }

    @Test
    void getEventsByDays_whenWeekStorageEmpty_thenReturnEmptyList() {
        persistentStorage.setWeekStorage(Week.builder().build());

        List<Event> events = persistentStorage.getEventsByDay(EVENT_DATE);

        assertTrue(events.isEmpty());
    }

    @Test
    void getEventsWeek_whenEventsInWeekStorage_thenReturnEventsForWeek() {
        persistentStorage.setWeekStorage(week);

        Map<String, List<Event>> eventsByWeek = persistentStorage.getEventsByWeek();

        assertTrue(eventsByWeek.containsKey(EVENT_DATE));
        assertEquals(event, eventsByWeek.get(EVENT_DATE).get(0));

    }

    @Test
    void addEventWithIncorrectTime_shouldBeaAddedEvent_thenReturnListOfEvents() {
        event.setTime("00:00");
        persistentStorage.addEvent(EVENT_DATE, "incorrect", EVENT_NAME);

        Map<String, List<Event>> eventsByWeek = persistentStorage.getEventsByWeek();

        assertTrue(eventsByWeek.containsKey(EVENT_DATE));
        assertEquals(event, eventsByWeek.get(EVENT_DATE).get(0));
    }

    @Test
    void setWeekStorage_whenInvalidJson_thenNotSetWeekStorage() {
        String invalidJson = "{invalidJson}";

        assertThrows(JsonSyntaxException.class, () -> persistentStorage.setWeekStorage(invalidJson));
        assertNull(persistentStorage.getWeekStorage());
    }

    @Test
    void addEvent_whenWeekStorageNotInitialization_thenInitializeWeekStorageAndAddEvent() {
        String event = persistentStorage.addEvent(EVENT_DATE, EVENT_TIME_FOR_STRING, EVENT_NAME);

        assertEquals(EVENT_DATE + " " + EVENT_TIME_FOR_TIME + " " + EVENT_NAME, event);
        assertNotNull(persistentStorage);
    }
}