package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.weeklyplan;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class WeeklyPlanTestData {
    static Stream<Arguments> providerWeeklyPlanExecutorTest() {
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
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnOneDayAndDuplicateDate_thenReturnLastDateEvents")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsOnOneDayAndDuplicateDate.json")
                        .expectedResult("Ваши события 31-10-2023 Ужин в ресторане 18:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsTwoDays_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsTwoEventsTwoDays.json")
                        .expectedResult("Ваши события 31-10-2023 Лекция 12:00, Свидание 16:00, 04-11-2023 Велотренировка 10:00, Поход за покупками 20:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsManyEventsManyDaysEventWithNoSortDate_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsManyEventsManyDaysEventWithNoSortDate.json")
                        .expectedResult("Ваши события 29-10-2023 Велотренировка 10:00, Поход за покупками 20:00, 31-10-2023 Учеба 10:00, Уборка 23:00, 01-11-2023 Лекция 12:00, Свидание 16:00, 04-11-2023 Купание кота 19:00, Свидание 20:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsManyEventsManyDaysEventWithNoSortDateAndNoSortEventTime_thenReturnAllEvent")
                        .jsonFileSource("action/weeklyplan/WeeklyPlanContainsManyEventsManyDaysEventWithNoSortDateAndNoSortEventTime.json")
                        .expectedResult("Ваши события 29-10-2023 Завтрак 10:00, Обед 12:00, Ужин 23:00, 01-11-2023 Завтрак 09:00, Свидание 16:00, Лекция 18:00")
                        .build())
        );
    }
}
