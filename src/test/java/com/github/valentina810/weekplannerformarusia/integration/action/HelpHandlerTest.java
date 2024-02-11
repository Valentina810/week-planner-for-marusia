package com.github.valentina810.weekplannerformarusia.integration.action;

import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.integration.action.parameterized.help.ParameterForHelpTest;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.session.ActionsStorage;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class HelpHandlerTest {

    @Autowired
    private ActionExecutor actionExecutor;

    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.integration.action.parameterized.help.HelpTestData#providerHelpTest")
    public void checkCommandHelp(ParameterForHelpTest parameterForHelpTest) {
        String json = FileReader.loadStringFromFile(parameterForHelpTest.getJsonFileSource())
                .replace("phrase", parameterForHelpTest.getPhrase())
                .replace("prevAction", parameterForHelpTest.getPrevActions());

        UserRequest userRequest = new UserRequest();
        userRequest.fillUserRequest(new Gson().fromJson(json, UserRequest.class));
        UserResponse userResponse = actionExecutor.createUserResponse(userRequest);
        Object sessionState = userResponse.getSession_state();

        assertAll(
                () -> assertEquals(parameterForHelpTest.getExpectedResponsePhrase(), userResponse.getResponse().getText()),
                () -> assertFalse(userResponse.getResponse().isEnd_session())
        );

        if (sessionState == null) assertEquals(parameterForHelpTest.getExpectedActions(), sessionState);
        else assertEquals(parameterForHelpTest.getExpectedActions(), ((ActionsStorage) sessionState).getActions());
    }
}