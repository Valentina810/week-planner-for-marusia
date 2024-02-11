package com.github.valentina810.weekplannerformarusia.integration.action;

import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.integration.action.parameterized.weeklyplan.ParameterForWeeklyPlanTest;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WeeklyPlanHandlerTest {

    @Autowired
    private ActionExecutor actionExecutor;

    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.integration.action.parameterized.weeklyplan.WeeklyPlanTestData#providerWeeklyPlanHandlerTest")
    public void checkWeeklyPlan(ParameterForWeeklyPlanTest parameterForWeeklyPlanTest) {
        UserRequest userRequest = new Gson().fromJson(
                FileReader.loadJsonFromFile(parameterForWeeklyPlanTest.getJsonFileSource()),
                UserRequest.class);

        actionExecutor.createUserResponse(userRequest);

//        assertEquals(parameterForWeeklyPlanTest.getExpectedResult(), actionExecutor.getUserResponse().getResponse().getText());
    }
}
