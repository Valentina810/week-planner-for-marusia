package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.service.parameterized.weeklyplan.ParameterForWeeklyPlanTest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WeeklyPlanHandlerTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.weeklyplan.WeeklyPlanTestData#providerWeeklyPlanHandlerTest")
    public void checkWeeklyPlan(ParameterForWeeklyPlanTest parameterForWeeklyPlanTest) {
        String request = FileReader.loadStringFromFile(parameterForWeeklyPlanTest.getJsonFileSource());
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterForWeeklyPlanTest.getExpectedResult(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }
}
