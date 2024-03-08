package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExitHandlerTest {

    @Autowired
    private WeekPlannerService weekPlannerService;

    // @Test
    @SneakyThrows
    public void getGoodbye_whenExitCommand_thenReturnMessageGoodbyeAndEndSession() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("phrase", "пока");
        UserRequest userRequest = new Gson().fromJson(json, UserRequest.class);
        Object response = weekPlannerService.getResponse(userRequest).getBody();
        JSONObject responseObject = new JSONObject(String.valueOf(response)).getJSONObject("response");
        Optional<JSONObject> state = Optional.ofNullable(responseObject.optJSONObject("state"));
        String stateUserActual = Optional.ofNullable(state
                .orElse(new JSONObject()).optJSONObject("user")).orElse(new JSONObject()).toString();
        String stateSessionActual = Optional.ofNullable(state
                .orElse(new JSONObject()).optJSONObject("session")).orElse(new JSONObject()).toString();

        assertAll(
                () -> assertEquals("До свидания!", responseObject.getString("text")),
                () -> assertTrue(responseObject.getBoolean("end_session")),
                () -> assertEquals("{}", stateUserActual),
                () -> assertEquals("{}", stateSessionActual)
        );
    }
}
