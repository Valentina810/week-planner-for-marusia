package com.github.valentina810.weekplannerformarusia.action.parameterized.weeklyplan;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class WeeklyPlanTestData {
    static Stream<Arguments> providerWeeklyPlanHandlerTest() {
        return Stream.of(
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanEmpty_thenReturnEmpty")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanEmpty.json")
                        .expectedResult("У вас пока нет событий на этой неделе").build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsOneEvent_thenReturnOneEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsOneEvent.json")
                        .expectedResult("Ваши события 31-10-2023 12:00 Лекция").build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnOneDay_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsOnOneDay.json")
                        .expectedResult("Ваши события 31-10-2023 12:00 Лекция 16:00 Свидание")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnDifferentDay_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsOnDifferentDay.json")
                        .expectedResult("Ваши события 31-10-2023 12:00 Лекция 03-11-2023 18:00 Ужин в ресторане")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsTwoDays_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsTwoDays.json")
                        .expectedResult("Ваши события 31-10-2023 12:00 Лекция 16:00 Свидание 04-11-2023 10:00 Велотренировка 20:00 Поход за покупками")
                        .build())
        );
    }
}
