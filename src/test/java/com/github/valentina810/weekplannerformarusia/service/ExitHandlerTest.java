package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExitHandlerTest {

    @Autowired
    private WeekPlannerService weekPlannerService;

    @Test
    @SneakyThrows
    public void getGoodbye_whenExitCommand_thenReturnMessageGoodbyeAndEndSession() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("phrase", "пока");
        Object response = weekPlannerService.getResponse(new Gson().fromJson(json, UserRequest.class)).getBody();
        JSONObject responseObject = new JSONObject(String.valueOf(response)).getJSONObject("response");

        assertAll(
                () -> assertEquals("До свидания!", responseObject.getString("text")),
                () -> assertTrue(responseObject.getBoolean("end_session"))
        );
    }
}
