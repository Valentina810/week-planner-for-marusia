package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                .replace("testDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .replace("testEvents", "")
                .replace("phrase", "пока");

        actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));

        assertAll(
                () -> assertEquals("До свидания!",
                        actionExecutor.getUserResponse().getResponse().getText()),
                () -> assertTrue(actionExecutor.getUserResponse().getResponse().isEnd_session())
        );
    }
}
