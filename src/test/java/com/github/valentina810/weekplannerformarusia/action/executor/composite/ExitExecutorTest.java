package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.util.FileReader;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitExecutorTest extends BaseTest {

    @Test
    @SneakyThrows
    public void getGoodbye_whenExitCommand_thenReturnMessageGoodbyeAndEndSession() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("phrase", "пока");
        JSONObject response = getResponse.apply(json);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals("До свидания!", objectResponse.getString("text")),
                () -> assertTrue(objectResponse.getBoolean("end_session")),
                () -> assertNull(getValue.apply(response, "user_state_update")),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }
}
