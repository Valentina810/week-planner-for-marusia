package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.service.parameterized.dayplan.ParameterForEventsForDateTest;
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
public class TomorrowPlanHandlerTest {

    @Autowired
    private WeekPlannerService weekPlannerService;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.dayplan.EventsForDateTestData#providerTomorrowPlanHandlerTest")
    public void checkTomorrowPlan(ParameterForEventsForDateTest parameterForEventsForDateTest) {
        String json = FileReader.loadStringFromFile(parameterForEventsForDateTest.getJsonFileSource())
                .replace("testDate", parameterForEventsForDateTest.getDate())
                .replace("testEvents", parameterForEventsForDateTest.getTodayEvents())
                .replace("phrase", parameterForEventsForDateTest.getPhrase());

        Object response = weekPlannerService.getResponse(new Gson().fromJson(json, UserRequest.class)).getBody();

        assertEquals(parameterForEventsForDateTest.getExpectedResult(),
                new JSONObject(String.valueOf(response)).getJSONObject("response").getString("text"));
    }
}