package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.github.valentina810.weekplannerformarusia.util.DateConverter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

import static java.time.format.TextStyle.FULL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersistentStorageTest {

    private PersistentStorage persistentStorage;

    private Week week;
    private static final String EVENT_DATE = DateConverter.convertDate.apply(LocalDate.now());
    private static final String EVENT_TIME_FOR_STRING = "одиннадцать часов тридцать восемь минут";
    private static final String EVENT_TIME_FOR_TIME = "11:38";
    private static final String EVENT_NAME = "Ужин";

    private static final Event event = Event.builder()
            .time(EVENT_TIME_FOR_TIME)
            .name(EVENT_NAME)
            .build();

    private final String timeZone = TimeZone.getDefault().getID();

    @BeforeAll
    void allSetup() {
        Map<String, List<Event>> days = new HashMap<>();
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

        assertAll(
                () -> assertNotNull(persistentStorage.getWeekStorage()),
                () -> assertEquals(week, persistentStorage.getWeekStorage().getWeek())
        );
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

        assertAll(
                () -> assertTrue(eventsByWeek.containsKey(EVENT_DATE)),
                () -> assertEquals(event, eventsByWeek.get(EVENT_DATE).getFirst())
        );

    }

    @Test
    void setWeekStorage_whenInvalidJson_thenNotSetWeekStorage() {
        String invalidJson = "{invalidJson}";

        assertAll(
                () -> assertThrows(JsonSyntaxException.class, () -> persistentStorage.setWeekStorage(invalidJson)),
                () -> assertNull(persistentStorage.getWeekStorage())
        );
    }

    @Test
    void addEvent_whenWeekStorageNotInitialization_thenInitializeWeekStorageAndAddEvent() {
        String event = persistentStorage.addEvent(EVENT_DATE, EVENT_TIME_FOR_STRING, EVENT_NAME, timeZone);

        assertAll(
                () -> assertEquals(EVENT_DATE + " " + EVENT_TIME_FOR_TIME + " " + EVENT_NAME, event),
                () -> assertNotNull(persistentStorage)
        );
    }

    @Test
    void addEventWithIncorrectTime_shouldBeaAddedEvent_thenReturnListOfEvents() {
        Event event = Event.builder()
                .time("0:00")
                .name(EVENT_NAME)
                .build();
        String day = getNextDayOfWeek("пятница");
        persistentStorage.addEvent("пятница", "incorrect", EVENT_NAME, timeZone);

        Map<String, List<Event>> eventsByWeek = persistentStorage.getEventsByWeek();

        assertAll(
                () -> assertTrue(eventsByWeek.containsKey(day)),
                () -> assertEquals(event, eventsByWeek.get(day).getFirst())
        );
    }

    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorageData#providerPersistentStorageTest")
    void addEventForDayOfWeek_thenReturnListOfEvents(String dayName) {
        String day = getNextDayOfWeek(dayName);
        Event event = Event.builder()
                .time(EVENT_TIME_FOR_TIME)
                .name(EVENT_NAME)
                .build();
        persistentStorage.addEvent(dayName, EVENT_TIME_FOR_STRING, EVENT_NAME, timeZone);

        Map<String, List<Event>> eventsByWeek = persistentStorage.getEventsByWeek();

        assertAll(
                () -> assertTrue(eventsByWeek.containsKey(day)),
                () -> assertEquals(event, eventsByWeek.get(day).getFirst())
        );
    }

    public static String getNextDayOfWeek(String dayOfWeekName) {
        LocalDate today = LocalDate.now();
        return Stream.of(DayOfWeek.values())
                .filter(day -> dayOfWeekName.contains(day.getDisplayName(FULL, new Locale("ru"))))
                .findFirst()
                .map(targetDay -> {
                    int daysUntilNextTarget = (targetDay.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
                    return daysUntilNextTarget == 0 ? today : today.plusDays(daysUntilNextTarget);
                })
                .orElse(today)
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}