package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.service.parameterized.dayplan.ParameterForEventsForDateTest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TomorrowPlanHandlerTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.dayplan.EventsForDateTestData#providerTomorrowPlanHandlerTest")
    public void checkTomorrowPlan(ParameterForEventsForDateTest parameterForEventsForDateTest) {
        String request = FileReader.loadStringFromFile(parameterForEventsForDateTest.getJsonFileSource())
                .replace("testDate", parameterForEventsForDateTest.getDate())
                .replace("testEvents", parameterForEventsForDateTest.getTodayEvents())
                .replace("phrase", parameterForEventsForDateTest.getPhrase());
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterForEventsForDateTest.getExpectedResult(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }
}