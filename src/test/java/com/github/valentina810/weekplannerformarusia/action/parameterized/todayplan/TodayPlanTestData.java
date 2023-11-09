package com.github.valentina810.weekplannerformarusia.action.parameterized.todayplan;

import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class TodayPlanTestData {
    private static final String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String templateJson = "action/planToday/TodayPlanTemplate.json";

    static Stream<Arguments> providerTodayPlanHandlerTest() {
        return Stream.of(
                Arguments.of(ParameterForTodayPlanTest.builder()
                        .testName("getPlanToday_whenPlanTodayEmpty_thenReturnEmpty")
                        .jsonFileSource("action/planToday/TodayPlanEmpty.json")
                        .todayDate(today)
                        .todayEvents("")
                        .expectedResult("У вас пока нет событий на сегодня")
                        .build()),
                Arguments.of(ParameterForTodayPlanTest.builder()
                        .testName("getPlanToday_whenPlanTodayContainsOneEvent_thenReturnOneEvent")
                        .jsonFileSource(templateJson)
                        .todayDate(today)
                        .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}")
                        .expectedResult("Ваши события " + today + " 12:00 Лекция")
                        .build()),
                Arguments.of(ParameterForTodayPlanTest.builder()
                        .testName("getPlanToday_whenPlanTodayContainsTwoEvents_thenReturnAllEvent")
                        .jsonFileSource(templateJson)
                        .todayDate(today)
                        .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}, {\"time\": \"16:00\", \"name\": \"Прогулка на берегу моря\"}")
                        .expectedResult("Ваши события " + today + " 12:00 Лекция 16:00 Прогулка на берегу моря")
                        .build())
        );
    }
}
