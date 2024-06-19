package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help;

import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class HelpTestData {
    private static final String JSON_FILE_SOURCE_WITH_PREV_ACTION = "action/help/PlanWithPrevAction.json";

    private static final Actions EXPECTED_ACTIONS = Actions.builder()
            .prevActions(List.of())
            .build();

    static Stream<Arguments> providerHelpTest() {

        return Stream.of(
                Arguments.of(ParameterWithPrevActionsTest.builder()
                        .testName("getHelpMessage_whenCallCommandHelp_thenReturnHelpMessage")
                        .jsonFileSource(JSON_FILE_SOURCE_WITH_PREV_ACTION)
                        .phrase("справка")
                        .prevActions("")
                        .expectedResponsePhrase("Мои команды: план на неделю, план на сегодня, план на завтра, добавь событие. Мой автор - Валентина.")
                        .expectedActions(EXPECTED_ACTIONS)
                        .build())

        );
    }
}