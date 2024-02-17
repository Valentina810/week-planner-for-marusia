package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.service.parameterized.unknown.ParameterForUnknownTest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UnknownHandlerTest {

    @Autowired
    private WeekPlannerService weekPlannerService;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.unknown.UnknownTestData#providerUnknownTest")
    public void checkUnknown(ParameterForUnknownTest parameterForUnknownTest) {
        String json = FileReader.loadStringFromFile(parameterForUnknownTest.getJsonFileSource())
                .replace("testDate", parameterForUnknownTest.getTestDate())
                .replace("testEvents", parameterForUnknownTest.getTestEvents())
                .replace("phrase", parameterForUnknownTest.getPhrase());
        Object response = weekPlannerService.getResponse(new Gson().fromJson(json, UserRequest.class)).getBody();
        JSONObject responseObject = new JSONObject(String.valueOf(response)).getJSONObject("response");

        assertAll(
                () -> assertEquals(parameterForUnknownTest.getExpectedResult(), responseObject.getString("text")),
                () -> assertFalse(responseObject.getBoolean("end_session"))
        );
    }
}
