package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import com.github.valentina810.weekplannerformarusia.storage.session.ActionsStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class HelpHandlerTest {

    @Autowired
    private ActionExecutor actionExecutor;

    private final String prevAction = "\"actions\":{" +
            "\"prevActions\":[" +
            "{" +
            "\"number\":0," +
            "\"operation\":\"HELP\"," +
            "\"valueAction\":\"\"" +
            "}" +
            "]" +
            "}";

    private final PrevAction expectedPrevAction = PrevAction.builder()
            .number(0)
            .operation(TypeAction.HELP)
            .valueAction("")
            .build();
    private final Actions expectedActions = Actions.builder()
            .prevActions(List.of(expectedPrevAction))
            .build();

    @Test
    public void writeCommandInStateSessionActions_whenReceivedParentalCommand_thenReturnParentalCommandMessagePositive() {
        String json = FileReader.loadStringFromFile("action/plantodate/PlanEmpty.json")
                .replace("phrase", "справка");

        actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));
        UserResponse userResponse = actionExecutor.getUserResponse();

        assertAll(
                () -> assertEquals("Выберите раздел: команды, добавить событие, об авторе, вернуться в главное меню",
                        userResponse.getResponse().getText()),
                () -> assertEquals(expectedActions, ((ActionsStorage) userResponse.getSession_state()).getActions()),
                () -> assertFalse(actionExecutor.getUserResponse().getResponse().isEnd_session())
        );
    }

    @Test
    public void getNestedCommandCommands_whenNestedCommandExists_thenReturnNestedCommandMessagePositive() {
        String json = FileReader.loadStringFromFile("action/help/PlanWithPrevAction.json")
                .replace("phrase", "команды")
                .replace("prevAction", prevAction);

        actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));
        UserResponse userResponse = actionExecutor.getUserResponse();

        assertAll(
                () -> assertEquals("Вот какие у меня есть команды: план на неделю, план на сегодня, план на завтра",
                        userResponse.getResponse().getText()),
                () -> assertEquals(expectedActions, ((ActionsStorage) userResponse.getSession_state()).getActions()),
                () -> assertFalse(actionExecutor.getUserResponse().getResponse().isEnd_session())
        );
    }
}