package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class HelpTestData {
    private static final String TEMPLATE_JSON = "action/plantodate/PlanEmpty.json";

    static Stream<Arguments> providerHelpTest() {

        return Stream.of(
                of(ParameterWithPrevActionsTest.builder()
                        .testName("getHelpMessage_whenCallCommandHelp_thenReturnHelpMessage")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("справка")
                        .expectedResponsePhrase("Мои команды: план на неделю, план на сегодня, план на завтра, добавь событие и справка. Чтобы выйти из навыка скажите выход.")
                        .build())

        );
    }
}