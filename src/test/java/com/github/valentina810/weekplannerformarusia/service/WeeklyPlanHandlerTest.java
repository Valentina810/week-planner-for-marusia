package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.service.parameterized.weeklyplan.ParameterForWeeklyPlanTest;
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
public class WeeklyPlanHandlerTest {

    @Autowired
    private WeekPlannerService weekPlannerService;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.weeklyplan.WeeklyPlanTestData#providerWeeklyPlanHandlerTest")
    public void checkWeeklyPlan(ParameterForWeeklyPlanTest parameterForWeeklyPlanTest) {
        String json = FileReader.loadStringFromFile(parameterForWeeklyPlanTest.getJsonFileSource());
        Object response = weekPlannerService.getResponse(new Gson().fromJson(json, UserRequest.class)).getBody();
        JSONObject responseObject = new JSONObject(String.valueOf(response)).getJSONObject("response");

        assertEquals(parameterForWeeklyPlanTest.getExpectedResult(), responseObject.getString("text"));
    }
}
