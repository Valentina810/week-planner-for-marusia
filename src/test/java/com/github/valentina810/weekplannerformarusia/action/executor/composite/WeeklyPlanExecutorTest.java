package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.weeklyplan.ParameterForWeeklyPlanTest;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WeeklyPlanExecutorTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.weeklyplan.WeeklyPlanTestData#providerWeeklyPlanExecutorTest")
    public void checkWeeklyPlan(ParameterForWeeklyPlanTest parameterForWeeklyPlanTest) {
        String request = parameterForWeeklyPlanTest.getJsonBody();
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterForWeeklyPlanTest.getExpectedResult(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.weeklyplan.WeeklyPlanTestData#providerDeletionOfObsoleteEventsInTheWeeklyPlanTest")
    public void checkTheDeletionOfObsoleteEventsInTheWeeklyPlan(ParameterForWeeklyPlanTest parameterForWeeklyPlanTest) {
        JSONObject response = getResponse.apply(parameterForWeeklyPlanTest.getJsonBody());
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterForWeeklyPlanTest.getExpectedResult(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }
}
