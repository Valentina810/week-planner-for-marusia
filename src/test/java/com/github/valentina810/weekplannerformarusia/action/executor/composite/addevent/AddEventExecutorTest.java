package com.github.valentina810.weekplannerformarusia.action.executor.composite.addevent;

import com.github.valentina810.weekplannerformarusia.action.executor.composite.BaseTest;
import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help.ParameterWithPrevActionsTest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.util.DateConverter;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;

import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddEventExecutorTest extends BaseTest {

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.addevent.AddEventData#providerIntermediateCommandAddEventTest")
    public void checkIntermediateCommandAddEvent(ParameterWithPrevActionsTest parameterWithPrevActionsTest) {
        String request = getRequestFromFile(parameterWithPrevActionsTest);
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        assertAll(
                () -> assertEquals(parameterWithPrevActionsTest.getExpectedResponsePhrase(), objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(getPersistentStorage(request), getPersistentStorage(response)),
                () -> assertEquals(parameterWithPrevActionsTest.getExpectedActions(), getSessionStorage(response).getActions())
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.addevent.AddEventData#providerTerminalCommandAddEventTest")
    public void checkTerminalCommandAddEvent(ParameterWithPrevActionsTest parameterWithPrevActionsTest) {
        String request = getRequestFromFile(parameterWithPrevActionsTest);
        JSONObject response = getResponse.apply(request);
        JSONObject objectResponse = getObjectResponse.apply(response);

        PersistentStorage persistentStorage = getPersistentStorage(request);
        String date = DateConverter.convertDate.apply(LocalDate.now().with(nextOrSame(WEDNESDAY)));
        persistentStorage.addEvent("среда", "двадцать три часа тридцать две минуты", "Оповестить всех о ярмарке");

        assertAll(
                () -> assertEquals(parameterWithPrevActionsTest.getExpectedResponsePhrase().replace("{eventDate}", date),
                        objectResponse.getString("text")),
                () -> assertFalse(objectResponse.getBoolean("end_session")),
                () -> assertEquals(persistentStorage, getPersistentStorage(response)),
                () -> assertNull(getValue.apply(response, "session_state"))
        );
    }
}