package com.github.valentina810.weekplannerformarusia.service.parameterized.weeklyplan;

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
                        .expectedResult("Ваши события 31-10-2023 Лекция 12:00").build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnOneDay_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsOnOneDay.json")
                        .expectedResult("Ваши события 31-10-2023 Лекция 12:00, Свидание 16:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnDifferentDay_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsOnDifferentDay.json")
                        .expectedResult("Ваши события 31-10-2023 Лекция 12:00, 03-11-2023 Ужин в ресторане 18:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsTwoDays_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsTwoDays.json")
                        .expectedResult("Ваши события 31-10-2023 Лекция 12:00, Свидание 16:00, 04-11-2023 Велотренировка 10:00, Поход за покупками 20:00")
                        .build())
        );
    }
}
