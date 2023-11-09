package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.parameterized.todayplan.ParameterForTodayPlanTest;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class TodayPlanHandlerTest {
    @Autowired
    private ActionExecutor actionExecutor;
    @Autowired
    private UserRequest userRequest;

    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.parameterized.todayplan.TodayPlanTestData#providerTodayPlanHandlerTest")
    public void checkTodayPlan(ParameterForTodayPlanTest parameterForTodayPlanTest) {
        String json = FileReader.loadStringFromFile(parameterForTodayPlanTest.getJsonFileSource())
                .replace("todayDate", parameterForTodayPlanTest.getTodayDate())
                .replace("todayEvents", parameterForTodayPlanTest.getTodayEvents());

        log.info("Из файла получен json {}", json);
        userRequest = new Gson().fromJson(json, UserRequest.class);
        log.info("userRequest = {}", new Gson().toJson(userRequest, JsonElement.class));
        actionExecutor.createUserResponse(userRequest);

        assertEquals(parameterForTodayPlanTest.getExpectedResult(),
                actionExecutor.getUserResponse().getResponse().getText());
    }
}