package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.weeklyplan;

import com.github.valentina810.weekplannerformarusia.util.DateConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.provider.Arguments;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.valentina810.weekplannerformarusia.util.FileReader.loadStringFromFile;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.params.provider.Arguments.of;

@Slf4j
public class WeeklyPlanTestData {
    private static final String TODAY = DateConverter.convertDate.apply(LocalDate.now());
    private static final String TOMORROW = DateConverter.convertDate.apply(LocalDate.now().plusDays(1));
    private static final String YESTERDAY = DateConverter.convertDate.apply(LocalDate.now().minusDays(1));

    private static final DateTimeFormatter pattern = ofPattern("dd-MM-yyyy");
    private static final String TODAY_DATE = LocalDate.now().format(pattern);
    private static final String TOMORROW_DATE = LocalDate.now().plusDays(1).format(pattern);
    private static final String YESTERDAY_DATE = LocalDate.now().minusDays(1).format(pattern);

    private static final Function<String, String> EVENTS_WITH_OBSOLETE_EVENTS_WITH_TIME_ZONE = timezone -> {
        LocalDate today = ZonedDateTime.now(ZoneId.of(timezone)).toLocalDate();
        return loadStringFromFile("action/weeklyplan/remove/eventsWithObsoleteEvents.json")
                .replace("today", today.format(pattern))
                .replace("yesterday", today.minusDays(1).format(pattern))
                .replace("tomorrow", today.plusDays(1).format(pattern));
    };

    private static final String EVENTS_WITH_OBSOLETE_EVENTS = loadStringFromFile("action/weeklyplan/remove/eventsWithObsoleteEvents.json")
            .replace("today", TODAY_DATE)
            .replace("yesterday", YESTERDAY_DATE)
            .replace("tomorrow", TOMORROW_DATE);

    private static final String EVENTS_WITHOUT_OBSOLETE_EVENTS = getJsonBody(loadStringFromFile("action/weeklyplan/remove/eventsWithoutObsoleteEvents.json")
            .replace("today", TODAY_DATE)
            .replace("tomorrow", TOMORROW_DATE));
    private static final String EVENTS_IN_ONE_DAY = getJsonBody(loadStringFromFile("action/weeklyplan/remove/eventsInOneDay.json"));
    private static final String EVENTS_ONLY_FROM_THE_FUTURE = EVENTS_IN_ONE_DAY
            .replace("date", TOMORROW_DATE);
    private static final String EVENTS_ONLY_IN_THE_PAST = EVENTS_IN_ONE_DAY
            .replace("date", YESTERDAY_DATE);
    private static final String EVENTS_ONLY_IN_THE_CURRENT_DAY = EVENTS_IN_ONE_DAY
            .replace("date", TODAY_DATE);
    private static final String EVENTS_EMPTY = getJsonBody(loadStringFromFile("action/weeklyplan/remove/emptyDays.json"));

    private static final String STATE_USER_EMPTY = getJsonBody("{}");
    private static final String STATE_USER_WEEK_EMPTY = getJsonBody("{\"week\": {}}");

    static Stream<Arguments> providerWeeklyPlanExecutorTest() {
        return Stream.of(
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanEmpty_thenReturnEmpty")
                        .jsonBody(getJsonBody(STATE_USER_EMPTY, 1))
                        .expectedResult("У вас пока нет событий на этой неделе").build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsOneEvent_thenReturnOneEvent")
                        .jsonBody(getJsonBody(loadStringFromFile("action/weeklyplan/weekContainsOneEvent.json"), 2))
                        .expectedResult("Ваши события на вторник тридцать первое октября 12:00 Лекция").build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnOneDay_thenReturnAllEvent")
                        .jsonBody(getJsonBody(loadStringFromFile("action/weeklyplan/weekContainsTwoEventsOnOneDay.json"), 3))
                        .expectedResult("Ваши события на вторник тридцать первое октября 12:00 Лекция, 16:00 Свидание")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnDifferentDay_thenReturnAllEvent")
                        .jsonBody(getJsonBody(loadStringFromFile("action/weeklyplan/weekContainsTwoEventsOnDifferentDay.json"), 4))
                        .expectedResult("Ваши события на вторник тридцать первое октября 12:00 Лекция, пятницу третье ноября 18:00 Ужин в ресторане")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsOnOneDayAndDuplicateDate_thenReturnLastDateEvents")
                        .jsonBody(getJsonBody(loadStringFromFile("action/weeklyplan/weekContainsTwoEventsOnOneDayAndDuplicateDate.json"), 5))
                        .expectedResult("Ваши события на вторник тридцать первое октября 18:00 Ужин в ресторане")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsTwoEventsTwoDays_thenReturnAllEvent")
                        .jsonBody(getJsonBody(loadStringFromFile("action/weeklyplan/weekContainsTwoEventsTwoDays.json"), 6))
                        .expectedResult("Ваши события на вторник тридцать первое октября 12:00 Лекция, 16:00 Свидание, субботу четвертое ноября 10:00 Велотренировка, 20:00 Поход за покупками")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsManyEventsManyDaysEventWithNoSortDate_thenReturnAllEvent")
                        .jsonBody(getJsonBody(loadStringFromFile("action/weeklyplan/weekContainsManyEventsManyDaysEventWithNoSortDate.json"), 7))
                        .expectedResult("Ваши события на воскресенье двадцать девятое октября 10:00 Велотренировка, 20:00 Поход за покупками, вторник тридцать первое октября 10:00 Учеба, 23:00 Уборка, среду первое ноября 12:00 Лекция, 16:00 Свидание, субботу четвертое ноября 19:00 Купание кота, 20:00 Свидание")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsManyEventsManyDaysEventWithNoSortDateAndNoSortEventTime_thenReturnAllEvent")
                        .jsonBody(getJsonBody(loadStringFromFile("action/weeklyplan/weekContainsManyEventsManyDaysEventWithNoSortDateAndNoSortEventTime.json"), 8))
                        .expectedResult("Ваши события на воскресенье двадцать девятое октября 10:00 Завтрак, 12:00 Обед, 23:00 Ужин, среду первое ноября 09:00 Завтрак, 16:00 Свидание, 18:00 Лекция")
                        .build())
        );
    }

    static Stream<Arguments> providerDeletionOfObsoleteEventsInTheWeeklyPlanTest() {
        return Stream.of(
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsObsoleteEvents_thenReturnTheWeeklyPlanWithoutOutdatedEvents")
                        .jsonBody(getJsonBody((EVENTS_WITH_OBSOLETE_EVENTS)))
                        .expectedResult("Ваши события на " + TODAY + " 10:00 Учеба, 23:00 Уборка, " + TOMORROW + " 10:00 Велотренировка, 20:00 Поход за покупками")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsObsoleteEventsAndMessageIdNoZero_thenReturnTheWeeklyPlanWithObsoleteEvents")
                        .jsonBody(getJsonBody(EVENTS_WITH_OBSOLETE_EVENTS, 1))
                        .expectedResult("Ваши события на " + YESTERDAY + " 19:00 Купание кота, 20:00 Свидание, " + TODAY + " 10:00 Учеба, 23:00 Уборка, " + TOMORROW + " 10:00 Велотренировка, 20:00 Поход за покупками")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanNoContainsObsoleteEvents_thenReturnTheWeeklyPlan")
                        .jsonBody(EVENTS_WITHOUT_OBSOLETE_EVENTS)
                        .expectedResult("Ваши события на " + TODAY + " 10:00 Учеба, 23:00 Уборка, " + TOMORROW + " 10:00 Велотренировка, 20:00 Поход за покупками")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsEventsFromTheFuture_thenReturnTheWeeklyPlan")
                        .jsonBody(EVENTS_ONLY_FROM_THE_FUTURE)
                        .expectedResult("Ваши события на " + TOMORROW + " 10:00 Велотренировка, 20:00 Поход за покупками")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsOnlyObsoleteEvents_thenReturnTheEmptyWeeklyPlan")
                        .jsonBody(EVENTS_ONLY_IN_THE_PAST)
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanContainsOnlyCurrentDayEvents_thenReturnTheWeeklyPlan")
                        .jsonBody(EVENTS_ONLY_IN_THE_CURRENT_DAY)
                        .expectedResult("Ваши события на " + TODAY + " 10:00 Велотренировка, 20:00 Поход за покупками")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeeklyPlanIsEmpty_thenReturnTheEmptyWeeklyPlan")
                        .jsonBody(EVENTS_EMPTY)
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenWeekIsEmpty_thenReturnTheEmptyWeeklyPlan")
                        .jsonBody(STATE_USER_EMPTY)
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build()),
                of(ParameterForWeeklyPlanTest.builder()
                        .testName("getWeeklyPlan_whenDaysIsEmpty_thenReturnTheEmptyWeeklyPlan")
                        .jsonBody(STATE_USER_WEEK_EMPTY)
                        .expectedResult("У вас пока нет событий на этой неделе")
                        .build())
        );
    }

    private static String getJsonBody(String week, int messageId) {
        return loadStringFromFile("action/weeklyplan/template.json")
                .replace("week", week)
                .replace("messageId", valueOf(messageId));
    }

    public static String getJsonBody(String week) {
        return getJsonBody(week, 0);
    }

    public static String getJsonBodyWithTimeZone(String timeZone) {
        return getJsonBody(EVENTS_WITH_OBSOLETE_EVENTS_WITH_TIME_ZONE.apply(timeZone), 0)
                .replace("Europe/Moscow", timeZone);
    }

    public static Stream<Arguments> providerTimeZoneTest() {
        return Stream.of(
                of(getExpectedDay("Europe/Moscow"), getJsonBodyWithTimeZone("Europe/Moscow")),
                of(getExpectedDay("Asia/Yerevan"), getJsonBodyWithTimeZone("Asia/Yerevan")),
                of(getExpectedDay("Australia/Sydney"), getJsonBodyWithTimeZone("Australia/Sydney")),
                of(getExpectedDay("Pacific/Auckland"), getJsonBodyWithTimeZone("Pacific/Auckland")),
                of(getExpectedDay("Europe/Paris"), getJsonBodyWithTimeZone("Europe/Paris"))
        );
    }

    /**
     * Получаем названия дней, на которые должны быть добавлены события
     */
    private static List<String> getExpectedDay(String timeZone) {
        LocalDate today = ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDate();
        LocalDate tomorrow = today.plusDays(1);

        return Stream.of(today, tomorrow)
                .map(e -> {
                    String dayName = e.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
                    return switch (dayName) {
                        case "среда" -> "среду";
                        case "пятница" -> "пятницу";
                        case "суббота" -> "субботу";
                        default -> dayName;
                    };
                })
                .collect(Collectors.toList());
    }
}
