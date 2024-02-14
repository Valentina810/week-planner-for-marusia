package com.github.valentina810.weekplannerformarusia.integration.action;

import com.github.valentina810.weekplannerformarusia.integration.action.parameterized.dayplan.ParameterForEventsForDateTest;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.service.WeekPlannerService;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TodayPlanHandlerTest {

    @Autowired
    private WeekPlannerService weekPlannerService;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.integration.action.parameterized.dayplan.EventsForDateTestData#providerTodayPlanHandlerTest")
    public void checkTodayPlan(ParameterForEventsForDateTest parameterForEventsForDateTest) {
        String json = FileReader.loadStringFromFile(parameterForEventsForDateTest.getJsonFileSource())
                .replace("testDate", parameterForEventsForDateTest.getDate())
                .replace("testEvents", parameterForEventsForDateTest.getTodayEvents())
                .replace("phrase", parameterForEventsForDateTest.getPhrase());
        Object response = weekPlannerService.getResponse(new Gson().fromJson(json, UserRequest.class)).getBody();

        assertEquals(parameterForEventsForDateTest.getExpectedResult(),
                new JSONObject(String.valueOf(response)).getJSONObject("response").getString("text"));
    }
}