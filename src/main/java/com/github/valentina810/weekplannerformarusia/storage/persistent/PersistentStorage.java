package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    //#todo тут могут храниться и другие данные, лучше сделать так, чтобы их не затирать
    @Getter
    private WeekStorage weekStorage;

    public void setWeekStorage(Week week) {
        weekStorage = WeekStorage.builder().week(week).build();
    }

    /**
     * Получить из хранилища массив с данными, если он есть
     * если массива с данными нет, то записать пустой
     */
    public void setWeekStorage(Object object) {//todo переписать  - пустое хранилище - не ошибка, просто нет событий, чтобы не хранить избыточную информацию
        try {//но если события есть, добавить сортировку по ключам от ранней даты к поздней
            JsonObject jsonObject = JsonParser.parseString(object.toString()).getAsJsonObject();
            Week week = new Gson().fromJson(jsonObject.get("week"), Week.class);
            if (week != null) {
                setWeekStorage(week);
                log.info("Получили данные о событиях на неделю из ответа");
            } else {
                log.info("Данные о событиях отсутствуют");
            }
        } catch (IllegalStateException | NullPointerException e) {
            log.info("В процессе получения данных о событиях из ответа возникла ошибка {}", e.getMessage());
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
        return weekStorage.getWeek().getDays();
    }

    /**
     * Добавление события в коллекцию
     */
    public void addEvent(String day, String time, String eventName) {
        String date = getDateEvent(day);
        if (weekStorage == null) {
            weekStorage = WeekStorage.builder().build();
        }
        weekStorage.addEvent(date, Event.builder().time(getTime(time)).name(eventName).build());
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
        return nextDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));//todo паттерн вынести
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

    /**
     * Получить время из строки
     *
     * @param time - время в формате "двенадцать часов ноль-ноль минут"
     * @return - время в формате 12:00
     */
    private String getTime(String time) {
        return "12:00";
    }
}
