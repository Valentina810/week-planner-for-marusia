package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown;

import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class UnknownTestData {
    private static final String EXPECTED_RESULT = "Получена неизвестная команда! Используйте команду справка для того чтобы узнать мои команды";
    private static final String NOW = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String JSON_FILE_SOURCE_EMPTY = "action/plantodate/PlanEmpty.json";

    static Stream<Arguments> providerUnknownTest() {

        return Stream.of(
                Arguments.of(ParameterForUnknownTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanEmpty_thenReturnEmpty")
                        .jsonFileSource(JSON_FILE_SOURCE_EMPTY)
                        .testDate(NOW)
                        .testEvents("")
                        .phrase("PhraseUnknown")
                        .expectedResult(EXPECTED_RESULT)
                        .build()),
                Arguments.of(ParameterForUnknownTest.builder()
                        .testName("getPhraseUnknownCommand_whenCommandEmpty_thenReturnMessage")
                        .jsonFileSource(JSON_FILE_SOURCE_EMPTY)
                        .testDate(NOW)
                        .testEvents("")
                        .phrase("")
                        .expectedResult(EXPECTED_RESULT)
                        .build())
        );
    }
}