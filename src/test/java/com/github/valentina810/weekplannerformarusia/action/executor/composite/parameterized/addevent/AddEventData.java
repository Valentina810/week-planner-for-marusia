package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.addevent;

import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help.ParameterWithPrevActionsTest;
import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_DAY;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_EVENT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_TIME;

public class AddEventData {
    private static final String JSON_FILE_SOURCE_WITH_PREV_ACTION = "action/help/PlanWithPrevAction.json";

    private static String EVENT_TIME = "двадцать три часа тридцать две минуты";
    private static String EVENT_NAME = "Оповестить всех о ярмарке";

    private static final String PREV_ACTIONS_ADD_EVENT = "\"actions\":{" +
            "\"prevActions\":[" +
            "{" +
            "\"number\":0," +
            "\"operation\":\"ADD_EVENT\"," +
            "\"valueAction\":\"добавь событие\"" +
            "}" +
            "]" +
            "}";

    private static final String PREV_ACTIONS_ADD_DAY = "\"actions\":{" +
            "\"prevActions\":[" +
            "{" +
            "\"number\":0," +
            "\"operation\":\"ADD_EVENT\"," +
            "\"valueAction\":\"добавь событие\"" +
            "}," +
            "{" +
            "\"number\":1," +
            "\"operation\":\"ADD_DAY\"," +
            "\"prevOperation\":\"ADD_EVENT\"," +
            "\"valueAction\":\"среда\"" +
            "}" +
            "]" +
            "}";

    private static final String PREV_ACTIONS_ADD_TIME = "\"actions\":{" +
            "\"prevActions\":[" +
            "{" +
            "\"number\":0," +
            "\"operation\":\"ADD_EVENT\"," +
            "\"valueAction\":\"добавь событие\"" +
            "}," +
            "{" +
            "\"number\":1," +
            "\"operation\":\"ADD_DAY\"," +
            "\"prevOperation\":\"ADD_EVENT\"," +
            "\"valueAction\":\"среда\"" +
            "}," +
            "{" +
            "\"number\":2," +
            "\"operation\":\"ADD_TIME\"," +
            "\"prevOperation\":\"ADD_DAY\"," +
            "\"valueAction\":\""+EVENT_TIME+"\"" +
            "}" +
            "]" +
            "}";

    private static final PrevAction PREV_ACTION_ADD_EVENT = PrevAction.builder()
            .number(0)
            .operation(ADD_EVENT)
            .valueAction("добавь событие")
            .build();

    private static final PrevAction PREV_ACTION_ADD_DAY = PrevAction.builder()
            .number(1)
            .operation(ADD_DAY)
            .prevOperation(ADD_EVENT)
            .valueAction("среда")
            .build();

    private static final PrevAction PREV_ACTION_ADD_TIME = PrevAction.builder()
            .number(2)
            .operation(ADD_TIME)
            .prevOperation(ADD_DAY)
            .valueAction(EVENT_TIME)
            .build();

    static Stream<Arguments> providerIntermediateCommandAddEventTest() {

        return Stream.of(
                Arguments.of(ParameterWithPrevActionsTest.builder()
                        .testName("writeCommandAddEventInStateSessionActions_whenSendCommandAddEvent_thenReturnCommandMessagePositiveWithRequestData")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("добавь событие")
                        .prevActions("")
                        .expectedResponsePhrase("Назовите день, например, среда ")
                        .expectedActions(Actions.builder()
                                .prevActions(List.of(PREV_ACTION_ADD_EVENT))
                                .build())
                        .build()),
                Arguments.of(ParameterWithPrevActionsTest.builder()
                        .testName("writeCommandAddDayInStateSessionActions_whenSendCommandAddDay_thenReturnCommandMessagePositiveWithRequestData")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("среда")
                        .prevActions(PREV_ACTIONS_ADD_EVENT)
                        .expectedResponsePhrase("Назовите время, например, 12 часов 30 минут ")
                        .expectedActions(Actions.builder()
                                .prevActions(List.of(PREV_ACTION_ADD_EVENT, PREV_ACTION_ADD_DAY))
                                .build())
                        .build()),
                Arguments.of(ParameterWithPrevActionsTest.builder()
                        .testName("writeCommandAddTimeInStateSessionActions_whenSendCommandAddTime_thenReturnCommandMessagePositiveWithRequestData")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase(EVENT_TIME)
                        .prevActions(PREV_ACTIONS_ADD_DAY)
                        .expectedResponsePhrase("Назовите название события ")
                        .expectedActions(Actions.builder()
                                .prevActions(List.of(PREV_ACTION_ADD_EVENT, PREV_ACTION_ADD_DAY, PREV_ACTION_ADD_TIME))
                                .build())
                        .build())
        );
    }

    static Stream<Arguments> providerTerminalCommandAddEventTest() {

        return Stream.of(
                Arguments.of(ParameterWithPrevActionsTest.builder()
                        .testName("writeNewEventInPersistentStorage_whenSendCommandAddNameEvent_thenReturnAddNewEventMessagePositive")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase(EVENT_NAME)
                        .prevActions(PREV_ACTIONS_ADD_TIME)
                        .expectedResponsePhrase("Событие успешно добавлено на {eventDate} 23:32 "+EVENT_NAME+"! Выполнен выход в главное меню ")
                        .build()));
    }
}