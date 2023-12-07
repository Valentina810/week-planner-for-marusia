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
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UnknownHandlerTest {
    @Autowired
    private ActionExecutor actionExecutor;

    @Test
    public void getPhraseUnknownCommand_whenUnknownCommand_thenReturnMessage() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("testDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .replace("testEvents", "")
                .replace("phrase", "PhraseUnknown");

        actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));

        assertAll(
                () -> assertEquals("Получена неизвестная команда",
                        actionExecutor.getUserResponse().getResponse().getText()),
                () -> assertFalse(actionExecutor.getUserResponse().getResponse().isEnd_session())
        );
    }

    @Test
    public void getPhraseUnknownCommand_whenCommandEmpty_thenReturnMessage() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("testDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .replace("testEvents", "")
                .replace("phrase", "");

        actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));

        assertAll(
                () -> assertEquals("Получена неизвестная команда",
                        actionExecutor.getUserResponse().getResponse().getText()),
                () -> assertFalse(actionExecutor.getUserResponse().getResponse().isEnd_session())
        );
    }
}
