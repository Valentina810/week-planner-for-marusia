package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.session.ActionsStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class HelpHandlerTest {

    @Autowired
    private ActionExecutor actionExecutor;

    @Test
    public void getNestedCommand_whenNestedCommandExists_thenReturnCurrentCommandMessagePositive() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("phrase", "справка");
        UserRequest userRequest = new Gson().fromJson(json, UserRequest.class);

        actionExecutor.createUserResponse(userRequest);
        UserResponse userResponse = actionExecutor.getUserResponse();
        PrevAction actual = ((ActionsStorage) userResponse.getSession_state())
                .getActions().getPrevActions().get(0);
        PrevAction expected = PrevAction.builder()
                .number(0)
                .operation(TypeAction.HELP)
                .valueAction("")
                .build();

        assertAll(
                () -> assertEquals("Выберите раздел: команды, добавить событие, об авторе, вернуться в главное меню",
                        userResponse.getResponse().getText()),
                () -> assertEquals(expected, actual),
                () -> assertFalse(actionExecutor.getUserResponse().getResponse().isEnd_session())
        );
    }
}