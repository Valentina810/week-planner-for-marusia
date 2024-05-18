package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.weeklyplan;

import com.github.valentina810.weekplannerformarusia.util.FileReader;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class WeeklyPlanTestData {
    private static final String TODAY = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String TOMORROW = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    private static final String YESTERDAY = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    private static final String EVENTS_WITH_OBSOLETE_EVENTS =
            " \"%s\": [  {  \"time\": \"23:00\",  \"name\": \"Уборка\"  },  {  \"time\": \"10:00\",  \"name\": \"Учеба\"  }  ],  \"%s\": [  {  \"time\": \"20:00\",  \"name\": \"Свидание\"  },  {  \"time\": \"19:00\",  \"name\": \"Купание кота\"  }  ],  \"%s\": [  {  \"time\": \"20:00\",  \"name\": \"Поход за покупками\"  },  {  \"time\": \"10:00\",  \"name\": \"Велотренировка\"  }  ]".formatted(TODAY, YESTERDAY, TOMORROW);

    private static final String EVENTS_WITHOUT_OBSOLETE_EVENTS =
            " \"%s\": [  {  \"time\": \"23:00\",  \"name\": \"Уборка\"  },  {  \"time\": \"10:00\",  \"name\": \"Учеба\"  }  ],  \"%s\": [  {  \"time\": \"20:00\",  \"name\": \"Поход за покупками\"  },  {  \"time\": \"10:00\",  \"name\": \"Велотренировка\"  }  ]".formatted(TODAY, TOMORROW);
    private static final String eventInOneDay = """
             "date": [  {  "time": "20:00",  "name": "Поход за покупками"  },  {  "time": "10:00",  "name": "Велотренировка"  }  ]\
            """;
    private static final String EVENTS_ONLY_FROM_THE_FUTURE = eventInOneDay.replace("date", TOMORROW);
    private static final String EVENTS_ONLY_IN_THE_PAST = eventInOneDay.replace("date", YESTERDAY);
    private static final String EVENTS_ONLY_IN_THE_CURRENT_DAY = eventInOneDay.replace("date", TODAY);
    private static final String EVENTS_EMPTY = "";

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

    static Stream<Arguments> providerWeeklyPlanExecutorWithMessageId0Test() {
        return Stream.of(
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsObsoleteEvents_thenReturnTheWeeklyPlanWithoutOutdatedEvents")
                        .jsonFileSource(getJsonBody(EVENTS_WITH_OBSOLETE_EVENTS))
                        .expectedResult("Ваши события " + TODAY + " Учеба 10:00, Уборка 23:00, " + TOMORROW + " Велотренировка 10:00, Поход за покупками 20:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsObsoleteEventsAndMessageIdNoZero_thenReturnTheWeeklyPlanWithObsoleteEvents")
                        .jsonFileSource(getJsonBody(EVENTS_WITH_OBSOLETE_EVENTS, 1))
                        .expectedResult("Ваши события " + YESTERDAY + " Купание кота 19:00, Свидание 20:00, " + TODAY + " Учеба 10:00, Уборка 23:00, " + TOMORROW + " Велотренировка 10:00, Поход за покупками 20:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanNoContainsObsoleteEvents_thenReturnTheWeeklyPlan")
                        .jsonFileSource(getJsonBody(EVENTS_WITHOUT_OBSOLETE_EVENTS))
                        .expectedResult("Ваши события " + TODAY + " Учеба 10:00, Уборка 23:00, " + TOMORROW + " Велотренировка 10:00, Поход за покупками 20:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsEventsFromTheFuture_thenReturnTheWeeklyPlan")
                        .jsonFileSource(getJsonBody(EVENTS_ONLY_FROM_THE_FUTURE))
                        .expectedResult("Ваши события " + TOMORROW + " Велотренировка 10:00, Поход за покупками 20:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsOnlyObsoleteEvents_thenReturnTheEmptyWeeklyPlan")
                        .jsonFileSource(getJsonBody(EVENTS_ONLY_IN_THE_PAST))
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsOnlyCurrentDayEvents_thenReturnTheWeeklyPlan")
                        .jsonFileSource(getJsonBody(EVENTS_ONLY_IN_THE_CURRENT_DAY))
                        .expectedResult("Ваши события " + TODAY + " Велотренировка 10:00, Поход за покупками 20:00")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanIsEmpty_thenReturnTheEmptyWeeklyPlan")
                        .jsonFileSource(getJsonBody(EVENTS_EMPTY))
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeekIsEmpty_thenReturnTheEmptyWeeklyPlan")
                        .jsonFileSource(FileReader.loadStringFromFile("action/weeklyplan/WeeklyPlanWithMessageId0WithoutWeek.json"))
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build()),
                Arguments.of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenDaysIsEmpty_thenReturnTheEmptyWeeklyPlan")
                        .jsonFileSource(FileReader.loadStringFromFile("action/weeklyplan/WeeklyPlanWithMessageId0WithoutDays.json"))
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build()));
    }

    private static String getJsonBody(String events, int messageId) {
        return FileReader.loadStringFromFile("action/weeklyplan/WeeklyPlanWithMessageId0.json")
                .replace("events", events)
                .replace("messageId", String.valueOf(messageId));
    }

    private static String getJsonBody(String events) {
        return getJsonBody(events, 0);
    }
}
