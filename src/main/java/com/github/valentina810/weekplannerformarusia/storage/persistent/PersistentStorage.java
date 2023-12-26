package com.github.valentina810.weekplannerformarusia.storage.persistent;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Хранилище, которое хранит данные в УЗ пользователя
 * поле user_state_update - передаем, получаем из state.user
 * лимит размера json-объекта user_state_update — 5 Кбайт
 * https://dev.vk.com/ru/marusia/session-state
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class PersistentStorage {

    //#todo тут могут храниться и другие данные, лучше сделать так, чтобы их не затирать
    @Getter
    private Object user_state_update;

    /**
     * Возвращает user_state_update в виде объекта WeekStorage
     */
    public WeekStorage getWeekStorage() {
        return new Gson().fromJson(new Gson().toJson(user_state_update), WeekStorage.class);
    }

    /**
     * Получить из хранилища массив с данными, если он есть
     * если массива с данными нет, то записать пустой
     */
    public void getWeekEvents(final Object object) {
        try {
            JsonElement jsonElement = new Gson()
                    .fromJson(new Gson().toJson(object), JsonElement.class);
            Week week = new Gson()
                    .fromJson(new Gson().toJson(jsonElement.getAsJsonObject()
                            .getAsJsonObject("week")), Week.class);
            if (week != null) {
                user_state_update = WeekStorage.builder().week(week).build();
                log.info("Получили данные о событиях на неделю из ответа");
            } else {
                user_state_update = generateEmptyWeeklyStorage();
                log.info("Данные о событиях отсутствуют, записали пустую структуру");
            }
        } catch (IllegalStateException | NullPointerException e) {
            log.info("В процессе получения данных о событиях из ответа возникла ошибка {}", e.getMessage());
            user_state_update = generateEmptyWeeklyStorage();
            log.info("Данные о событиях отсутствуют, записали пустую структуру");
        }
    }

    /**
     * Заполнение хранилища пустой структурой с данными
     *
     * @return - объект-хранилище
     */
    private WeekStorage generateEmptyWeeklyStorage() {
        int dayInMilliseconds = 86400000;
        return WeekStorage.builder().week(Week.builder()
                .days(List.of(IntStream.range(0, 7)
                        .mapToObj(i -> {
                            return Day.builder()
                                    .date(new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis() + (long) i * dayInMilliseconds))
                                    .events(new ArrayList<>()).build();
                        }).toArray(Day[]::new))).build()).build();
    }
}
