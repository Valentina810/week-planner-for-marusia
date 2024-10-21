package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.github.valentina810.weekplannerformarusia.util.DateConverter;
import com.github.valentina810.weekplannerformarusia.util.Formatter;
import com.github.valentina810.weekplannerformarusia.util.TimeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.format.TextStyle.FULL;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.Locale.forLanguageTag;
import static java.util.Objects.requireNonNull;


/**
 * Хранилище, которое хранит данные в УЗ пользователя
 * поле user_state_update - передаем, получаем из state.user
 * лимит размера json-объекта user_state_update — 5 Кбайт
 * https://dev.vk.com/ru/marusia/session-state
 */

@Slf4j
@ToString
@EqualsAndHashCode
public class PersistentStorage {

    @Getter
    private WeekStorage weekStorage;

    public void setWeekStorage(Week week) {
        weekStorage = WeekStorage.builder().week(week).build();
    }

    /**
     * Получить из хранилища массив с данными, если он есть
     * если массива с данными нет, то записать пустой
     */
    public void setWeekStorage(Object object) {
        try {
            JsonObject jsonObject = JsonParser.parseString(object.toString()).getAsJsonObject();
            Optional.ofNullable(jsonObject.get("week"))
                    .map(JsonObject.class::cast)
                    .map(week -> new Gson().fromJson(week, Week.class))
                    .ifPresentOrElse(
                            this::setWeekStorage,
                            () -> log.info("Данные о событиях отсутствуют")
                    );
        } catch (IllegalStateException | NullPointerException e) {
            log.info("В процессе получения данных о событиях из ответа возникла ошибка " + e.getMessage());
        }
    }

    public WeekStorage getUser_state_update() {
        return weekStorage;
    }

    /**
     * Возвращает события за определённый день
     */
    public List<Event> getEventsByDay(String date) {
        return Optional.ofNullable(getWeekStorage())
                .map(WeekStorage::getWeek)
                .map(Week::getDays)
                .map(days -> days.get(date))
                .orElse(new ArrayList<>())
                .stream()
                .sorted(comparing(Event::getTime))
                .toList();
    }

    /**
     * Возвращает события за все дни
     */
    public Map<String, List<Event>> getEventsByWeek() {
        return weekStorage != null ? weekStorage.getWeek().getDays() : null;
    }

    /**
     * Добавление события в коллекцию
     */
    public String addEvent(String day, String time, String eventName, String zoneId) {
        if (weekStorage == null) {
            weekStorage = WeekStorage.builder().build();
        }
        String eventDate = getDateEvent(day, zoneId);
        String eventTime = TimeConverter.getTime(time);
        weekStorage.addEvent(eventDate, Event.builder().time(eventTime).name(eventName).build());
        return DateConverter.convertDate.apply(Formatter.convertStringToDate.apply(eventDate)) + " " + eventTime + " " + eventName;
    }

    /**
     * Получить дату ближаишего дня недели day
     *
     * @param day - день недели
     * @return - дата в формате dd-MM-yyyy
     */
    private String getDateEvent(String day, String zoneId) {
        LocalDate nextDate;
        LocalDate currentUserDate = Formatter.getCurrentDateForTimeZone.apply(zoneId);
        try {
            nextDate = currentUserDate.with(nextOrSame(parseDayOfWeek(day)));
        } catch (IllegalArgumentException | NullPointerException e) {
            nextDate = currentUserDate;
        }
        return Formatter.convertDateToString.apply(nextDate);
    }

    /**
     * Метод для парсинга названия дня недели на русском
     */
    private static DayOfWeek parseDayOfWeek(String dayOfWeek) {
        requireNonNull(dayOfWeek, "dayOfWeek не может быть null");
        return stream(DayOfWeek.values())
                .filter(dow -> dayOfWeek.contains(dow.getDisplayName(FULL, forLanguageTag("ru")).toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Недопустимое название дня недели: " + dayOfWeek));
    }

    /**
     * Удалить из храниища все события, с датой, менее текущей
     */
    public void removeObsoleteEvents(LocalDate currentDate) {
        if (weekStorage != null) {
            weekStorage.removeObsoleteEvents(currentDate);
        }
    }
}
