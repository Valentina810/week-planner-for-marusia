package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.github.valentina810.weekplannerformarusia.util.Formatter;
import com.github.valentina810.weekplannerformarusia.util.TimeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;


/**
 * Хранилище, которое хранит данные в УЗ пользователя
 * поле user_state_update - передаем, получаем из state.user
 * лимит размера json-объекта user_state_update — 5 Кбайт
 * https://dev.vk.com/ru/marusia/session-state
 */

@Slf4j
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
                .orElse(new ArrayList<>());
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
    public String addEvent(String day, String time, String eventName) {
        if (weekStorage == null) {
            weekStorage = WeekStorage.builder().build();
        }
        String eventDate = getDateEvent(day);
        String eventTime = TimeConverter.getTime(time);
        weekStorage.addEvent(eventDate, Event.builder().time(eventTime).name(eventName).build());
        return eventDate + " " + eventTime + " " + eventName;
    }

    /**
     * Получить дату ближаишего дня недели day
     *
     * @param day - день недели
     * @return - дата в формате dd-MM-yyyy
     */
    private String getDateEvent(String day) {
        LocalDate nextDate;
        try {
            nextDate = LocalDate.now().with(TemporalAdjusters.nextOrSame(parseDayOfWeek(day)));
        } catch (IllegalArgumentException e) {
            nextDate = LocalDate.now();
        }
        return Formatter.convertDateToString.apply(nextDate);
    }

    /**
     * Метод для парсинга названия дня недели на русском
     */
    private static DayOfWeek parseDayOfWeek(String dayOfWeek) {
        for (DayOfWeek dow : DayOfWeek.values()) {
            if (dow.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")).equalsIgnoreCase(dayOfWeek)) {
                return dow;
            }
        }
        throw new IllegalArgumentException("Недопустимое название дня недели: " + dayOfWeek);
    }
}
