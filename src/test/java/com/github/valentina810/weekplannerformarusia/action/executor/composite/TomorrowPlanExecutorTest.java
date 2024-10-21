package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.dayplan.ParameterForEventsForDateTest;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TomorrowPlanExecutorTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.dayplan.EventsForDateTestData#providerTomorrowPlanExecutorTest")
    public void checkTomorrowPlan(ParameterForEventsForDateTest parameterForEventsForDateTest) {
        String request = getRequestFromFile(parameterForEventsForDateTest);
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterForEventsForDateTest.getExpectedResult(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.dayplan.EventsForDateTestData#providerTimeZoneTest")
    void testGetTomorrowPlan_whenCorrectTimeZone_thenReturnEvent(String timezone) {
        String request = getRequestFromFile(ParameterForEventsForDateTest.builder()
                .jsonFileSource("action/plantodate/PlanTemplate.json")
                .date(ZonedDateTime.now(ZoneId.of(timezone)).toLocalDate().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}")
                .phrase("план на завтра")
                .build());
        request = request.replace("Europe/Moscow", timezone);
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        String text = objectResponse.getString("text");
        assertAll(
                () -> assertTrue(text.contains("Ваши события на завтра")),
                () -> assertTrue(text.contains("12:00 Лекция"))
        );
    }

    @SneakyThrows
    @Test
    void testGetTomorrowPlan_whenInCorrectTimeZone_thenReturnEvent() {
        String incorrectTimeZone = "Asia/Pukasia";
        String request = getRequestFromFile(ParameterForEventsForDateTest.builder()
                .jsonFileSource("action/plantodate/PlanTemplate.json")
                .date(LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}")
                .phrase("план на завтра")
                .build());
        request = request.replace("Europe/Moscow", incorrectTimeZone);
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        String text = objectResponse.getString("text");
        assertAll(
                () -> assertTrue(text.contains("Ваши события на завтра")),
                () -> assertTrue(text.contains("12:00 Лекция"))
        );
    }
}