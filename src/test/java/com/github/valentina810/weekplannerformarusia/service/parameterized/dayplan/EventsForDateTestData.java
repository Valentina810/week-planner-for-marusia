package com.github.valentina810.weekplannerformarusia.service.parameterized.dayplan;

import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class EventsForDateTestData {
    private static final String TODAY = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String TOMORROW = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String TEMPLATE_JSON = "action/plantodate/PlanTemplate.json";

    static Stream<Arguments> providerTodayPlanHandlerTest() {
        return Stream.of(
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanToday_whenPlanTodayEmpty_thenReturnEmpty")
                        .jsonFileSource("action/plantodate/PlanEmpty.json")
                        .phrase("план на сегодня")
                        .date(TODAY)
                        .todayEvents("")
                        .expectedResult("У вас пока нет событий на сегодня")
                        .build()),
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanToday_whenPlanTodayContainsOneEvent_thenReturnOneEvent")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("план на сегодня")
                        .date(TODAY)
                        .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}")
                        .expectedResult("Ваши события на сегодня " + TODAY + " 12:00 Лекция")
                        .build()),
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanToday_whenPlanTodayContainsTwoEvents_thenReturnAllEvent")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("план на сегодня")
                        .date(TODAY)
                        .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}, {\"time\": \"16:00\", \"name\": \"Прогулка на берегу моря\"}")
                        .expectedResult("Ваши события на сегодня " + TODAY + " 12:00 Лекция, 16:00 Прогулка на берегу моря")
                        .build()),
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanToday_whenPlanTodayContainsManyEventsNoSortEventTime_thenReturnAllEvent")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("план на сегодня")
                        .date(TODAY)
                        .todayEvents("{\"time\": \"18:00\", \"name\": \"Лекция\"}, {\"time\": \"06:00\", \"name\": \"Зарядка\"}, {\"time\": \"16:00\", \"name\": \"Прогулка на берегу моря\"}")
                        .expectedResult("Ваши события на сегодня " + TODAY + " 06:00 Зарядка, 16:00 Прогулка на берегу моря, 18:00 Лекция")
                        .build())
        );
    }

    static Stream<Arguments> providerTomorrowPlanHandlerTest() {
        return Stream.of(
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanTomorrow_whenPlanTomorrowEmpty_thenReturnEmpty")
                        .jsonFileSource("action/plantodate/PlanEmpty.json")
                        .phrase("план на завтра")
                        .date(TOMORROW)
                        .todayEvents("")
                        .expectedResult("У вас пока нет событий на завтра")
                        .build()),
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanTomorrow_whenPlanTomorrowContainsOneEvent_thenReturnOneEvent")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("план на завтра")
                        .date(TOMORROW)
                        .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}")
                        .expectedResult("Ваши события на завтра " + TOMORROW + " 12:00 Лекция")
                        .build()),
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanTomorrow_whenPlanTomorrowContainsTwoEvents_thenReturnAllEvent")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("план на завтра")
                        .date(TOMORROW)
                        .todayEvents("{\"time\": \"12:00\", \"name\": \"Лекция\"}, {\"time\": \"16:00\", \"name\": \"Прогулка на берегу моря\"}")
                        .expectedResult("Ваши события на завтра " + TOMORROW + " 12:00 Лекция, 16:00 Прогулка на берегу моря")
                        .build()),
                Arguments.of(ParameterForEventsForDateTest.builder()
                        .testName("getPlanTomorrow_whenPlanTomorrowContainsManyEventsNoSortEventTime_thenReturnAllEvent")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("план на завтра")
                        .date(TOMORROW)
                        .todayEvents("{\"time\": \"18:00\", \"name\": \"Лекция\"}, {\"time\": \"16:00\", \"name\": \"Прогулка на берегу моря\"}, {\"time\": \"06:00\", \"name\": \"Зарядка\"}")
                        .expectedResult("Ваши события на завтра " + TOMORROW + " 06:00 Зарядка, 16:00 Прогулка на берегу моря, 18:00 Лекция")
                        .build())
        );
    }
}
