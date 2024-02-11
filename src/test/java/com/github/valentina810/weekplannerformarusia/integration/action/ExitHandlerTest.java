package com.github.valentina810.weekplannerformarusia.integration.action;

import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ExitHandlerTest {
    @Autowired
    private ActionExecutor actionExecutor;

    @Test
    public void getGoodbye_whenExitCommand_thenReturnMessageGoodbyeAndEndSession() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("phrase", "пока");

        UserResponse userResponse = actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));

        assertAll(
                () -> assertEquals("До свидания!",
                        userResponse.getResponse().getText()),
                () -> assertTrue(userResponse.getResponse().isEnd_session())
        );
    }
}
