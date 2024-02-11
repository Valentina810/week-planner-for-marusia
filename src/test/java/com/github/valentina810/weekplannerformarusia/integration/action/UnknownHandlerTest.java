package com.github.valentina810.weekplannerformarusia.integration.action;

import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.integration.action.parameterized.unknown.ParameterForUnknownTest;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UnknownHandlerTest {
    @Autowired
    private ActionExecutor actionExecutor;

    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.integration.action.parameterized.unknown.UnknownTestData#providerUnknownTest")
    public void checkUnknown(ParameterForUnknownTest parameterForUnknownTest) {
        String json = FileReader.loadStringFromFile(parameterForUnknownTest.getJsonFileSource())
                .replace("testDate", parameterForUnknownTest.getTestDate())
                .replace("testEvents", parameterForUnknownTest.getTestEvents())
                .replace("phrase", parameterForUnknownTest.getPhrase());

        actionExecutor.createUserResponse(new Gson().fromJson(json, UserRequest.class));

//        assertAll(
//                () -> assertEquals(parameterForUnknownTest.getExpectedResult(),
//                        actionExecutor.getUserResponse().getResponse().getText()),
//                () -> assertFalse(actionExecutor.getUserResponse().getResponse().isEnd_session())
//        );
    }
}
