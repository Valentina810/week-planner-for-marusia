package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown.ParameterForUnknownTest;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UnknownExecutorTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown.UnknownTestData#providerUnknownTest")
    public void checkUnknown(ParameterForUnknownTest parameter) {
        String request = FileReader.loadStringFromFile(parameter.getJsonFileSource())
                .replace("testDate", parameter.getTestDate())
                .replace("testEvents", "")
                .replace("phrase", parameter.getPhrase());
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameter.getExpectedResult(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }
}
