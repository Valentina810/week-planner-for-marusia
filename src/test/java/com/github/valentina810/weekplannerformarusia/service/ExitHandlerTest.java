package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitHandlerTest extends BaseTest {

    @Test
    @SneakyThrows
    public void getGoodbye_whenExitCommand_thenReturnMessageGoodbyeAndEndSession() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("phrase", "пока");
        JSONObject response = new JSONObject(String.valueOf(weekPlannerService
                .getResponse(new Gson()
                        .fromJson(json, UserRequest.class))
                .getBody()))
                .getJSONObject("response");

        assertAll(
                () -> assertEquals("До свидания!", response.getString("text")),
                () -> assertTrue(response.getBoolean("end_session")),
                () -> assertNull(getStateValue.apply(response, "user")),
                () -> assertNull(getStateValue.apply(response, "session"))
        );
    }
}
