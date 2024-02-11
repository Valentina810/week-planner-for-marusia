package com.github.valentina810.weekplannerformarusia.integration.action;

import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.integration.action.parameterized.dayplan.ParameterForEventsForDateTest;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TomorrowPlanHandlerTest {
    @Autowired
    private ActionExecutor actionExecutor;

    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.integration.action.parameterized.dayplan.EventsForDateTestData#providerTomorrowPlanHandlerTest")
    public void checkTomorrowPlan(ParameterForEventsForDateTest parameterForEventsForDateTest) {
        String json = FileReader.loadStringFromFile(parameterForEventsForDateTest.getJsonFileSource())
                .replace("testDate", parameterForEventsForDateTest.getDate())
                .replace("testEvents", parameterForEventsForDateTest.getTodayEvents())
                .replace("phrase", parameterForEventsForDateTest.getPhrase());

        actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));

//        assertEquals(parameterForEventsForDateTest.getExpectedResult(),
//                actionExecutor.getUserResponse().getResponse().getText());
    }
}