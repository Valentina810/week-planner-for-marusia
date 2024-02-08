package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.parameterized.help.ParameterForHelpTest;
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
//
//    @Autowired
//    private ActionExecutor actionExecutor;
//    @Autowired
//    private UserRequest userRequest;
//
//    @ParameterizedTest
//    @MethodSource("com.github.valentina810.weekplannerformarusia.action.parameterized.help.HelpTestData#providerHelpTest")
//    public void checkCommandHelp(ParameterForHelpTest parameterForHelpTest) {
//        String json = FileReader.loadStringFromFile(parameterForHelpTest.getJsonFileSource())
//                .replace("phrase", parameterForHelpTest.getPhrase())
//                .replace("prevAction", parameterForHelpTest.getPrevActions());
//
//        userRequest.fillUserRequest(new Gson().fromJson(json, UserRequest.class));
//        actionExecutor.createUserResponse(userRequest);
//        UserResponse userResponse = actionExecutor.getUserResponse();
//
//        assertAll(
//                () -> assertEquals(parameterForHelpTest.getExpectedResponsePhrase(), userResponse.getResponse().getText()),
//                () -> assertEquals(parameterForHelpTest.getExpectedActions(), ((ActionsStorage) userResponse.getSession_state()).getActions()),
//                () -> assertFalse(actionExecutor.getUserResponse().getResponse().isEnd_session())
//        );
//    }
}