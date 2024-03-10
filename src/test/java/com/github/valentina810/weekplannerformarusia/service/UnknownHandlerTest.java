package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.service.parameterized.unknown.ParameterForUnknownTest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnknownHandlerTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.unknown.UnknownTestData#providerUnknownTest")
    public void checkUnknown(ParameterForUnknownTest parameterForUnknownTest) {
        String request = FileReader.loadStringFromFile(parameterForUnknownTest.getJsonFileSource())
                .replace("testDate", parameterForUnknownTest.getTestDate())
                .replace("testEvents", parameterForUnknownTest.getTestEvents())
                .replace("phrase", parameterForUnknownTest.getPhrase());
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterForUnknownTest.getExpectedResult(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }
}
