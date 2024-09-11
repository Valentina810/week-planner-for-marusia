package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@ToString
public class ParameterForUnknownTest {
    private String testName;
    private final String jsonFileSource = "action/plantodate/PlanEmpty.json";
    private final String testDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private final String testEvents = "";
    private String phrase;
    private final String expectedResult = "Получена неизвестная команда! Используйте команду справка для того чтобы узнать мои команды или скажите выход для выхода из навыка";
}