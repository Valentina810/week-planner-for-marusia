package com.github.valentina810.weekplannerformarusia.context;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Хранилище, которое хранит данные в УЗ пользователя
 * поле user_state_update
 * лимит размера json-объекта user_state_update — 5 Кбайт
 * https://dev.vk.com/ru/marusia/session-state
 */
@Slf4j
@Builder
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class PersistentStorage {

    private Object user_state_update;//#todo тут могут храниться и другие данные, лучше сделать так, чтобы их не затирать

    /**
     * Получить из хранилища массив с данными, если он есть
     * если массива с данными нет, то записать пустой
     */
    public void getWeekEvents() {
        JSONObject jsonObject;
        try {
            jsonObject = new Gson().fromJson(new Gson().toJson(user_state_update), JSONObject.class);
            if (jsonObject != null) {
                //читаем данные
            } else {
                user_state_update = generateEmptyWeeklyStorage();
            }
        } catch (JSONException | NullPointerException e) {
            user_state_update = generateEmptyWeeklyStorage();
        }
    }

    /**
     * Заполнение хранилища пустой структурой с данными
     *
     * @return - объект-хранилище
     */
    private WeekStorage generateEmptyWeeklyStorage() {
        int dayInMilliseconds = 86400000;
        return WeekStorage.builder().week(Week.builder().days(List.of(IntStream.range(0, 7).mapToObj(i -> {
            return Day.builder().date(new SimpleDateFormat("dd-MM-yyyy").format(System.currentTimeMillis() + i * dayInMilliseconds)).events(new ArrayList<>()).build();
        }).toArray(Day[]::new))).build()).build();
    }

    public WeekStorage getWeekStorage() {
        return new Gson().fromJson(new Gson().toJson(user_state_update), WeekStorage.class);
    }
}
