package com.github.valentina810.weekplannerformarusia.integration.action.parameterized.help;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class HelpTestData {

    private static final String JSON_FILE_SOURCE_WITH_PREV_ACTION = "action/help/PlanWithPrevAction.json";

    private static final String PREV_ACTIONS = "\"actions\":{" +
            "\"prevActions\":[" +
            "{" +
            "\"number\":0," +
            "\"operation\":\"HELP\"," +
            "\"valueAction\":\"справка\"" +
            "}" +
            "]" +
            "}";

    private static final PrevAction EXPECTED_PREV_ACTION = PrevAction.builder()
            .number(0)
            .operation(TypeAction.HELP)
            .valueAction("справка")
            .build();

    private static final Actions EXPECTED_ACTIONS = Actions.builder()
            .prevActions(List.of(EXPECTED_PREV_ACTION))
            .build();

    static Stream<Arguments> providerHelpTest() {

        return Stream.of(
                Arguments.of(ParameterForHelpTest.builder()
                        .testName("writeCommandInStateSessionActions_whenReceivedParentalCommand_thenReturnParentalCommandMessagePositive")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("справка")
                        .prevActions("")
                        .expectedResponsePhrase("Выберите раздел: команды, добавить событие, об авторе, вернуться в главное меню")
                        .expectedActions(EXPECTED_ACTIONS)
                        .build()),
                Arguments.of(ParameterForHelpTest.builder()
                        .testName("getNestedCommandCommands_whenNestedCommandExists_thenReturnNestedCommandMessagePositive")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("команды")
                        .prevActions(PREV_ACTIONS)
                        .expectedResponsePhrase("Вот какие у меня есть команды: план на неделю, план на сегодня, план на завтра")
                        .expectedActions(EXPECTED_ACTIONS)
                        .build()),
                Arguments.of(ParameterForHelpTest.builder()
                        .testName("getNestedCommandHowAddEvent_whenNestedCommandExists_thenReturnNestedCommandMessagePositive")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("как добавить")
                        .prevActions(PREV_ACTIONS)
                        .expectedResponsePhrase("Для того чтобы добавить событие скажите: добавь событие")
                        .expectedActions(EXPECTED_ACTIONS)
                        .build()),
                Arguments.of(ParameterForHelpTest.builder()
                        .testName("getNestedCommandAuthor_whenNestedCommandExists_thenReturnNestedCommandMessagePositive")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("об авторе")
                        .prevActions(PREV_ACTIONS)
                        .expectedResponsePhrase("Имя автора Валентина")
                        .expectedActions(EXPECTED_ACTIONS)
                        .build()),
                Arguments.of(ParameterForHelpTest.builder()
                        .testName("getNestedCommandExitMainMenu_whenNestedCommandExists_thenReturnNestedCommandMessagePositive")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("вернуться в главное меню")
                        .prevActions(PREV_ACTIONS)
                        .expectedResponsePhrase("Выполнен выход в главное меню")
                        .expectedActions(null)
                        .build())
        );
    }
}