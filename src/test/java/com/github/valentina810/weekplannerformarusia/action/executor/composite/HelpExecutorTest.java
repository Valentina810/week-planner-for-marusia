package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help.ParameterWithPrevActionsTest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HelpExecutorTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help.HelpTestData#providerHelpTest")
    public void checkCommandHelp(ParameterWithPrevActionsTest parameterWithPrevActionsTest) {
        String request = FileReader
                .loadStringFromFile(parameterWithPrevActionsTest.getJsonFileSource())
                .replace("phrase", parameterWithPrevActionsTest.getPhrase())
                .replace("prevAction", parameterWithPrevActionsTest.getPrevActions());
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterWithPrevActionsTest.getExpectedResponsePhrase(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertEquals(parameterWithPrevActionsTest.getExpectedActions(), getSessionStorage(response).getActions())
        );
    }
}