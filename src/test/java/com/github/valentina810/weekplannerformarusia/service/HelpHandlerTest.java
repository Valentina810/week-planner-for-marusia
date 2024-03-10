package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.service.parameterized.help.ParameterForHelpTest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HelpHandlerTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.help.HelpTestData#providerHelpTest")
    public void checkCommandHelp(ParameterForHelpTest parameterForHelpTest) {
        String request = FileReader
                .loadStringFromFile(parameterForHelpTest.getJsonFileSource())
                .replace("phrase", parameterForHelpTest.getPhrase())
                .replace("prevAction", parameterForHelpTest.getPrevActions());
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterForHelpTest.getExpectedResponsePhrase(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertEquals(parameterForHelpTest.getExpectedActions(), getSessionStorage(response).getActions())
        );
    }
}