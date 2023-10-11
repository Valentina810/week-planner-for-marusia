package com.github.valentina810.weekplannerformarusia.context;

import com.github.valentina810.weekplannerformarusia.model.DayWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

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
public class PersistentStorage {

    private DayWeek[] data;

    public DayWeek[] getDayWeeks(JSONObject jsonObject) {
        DayWeek[] dayWeeks = new DayWeek[7];
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("state").getJSONObject("user").getJSONArray("data");
            for (int i = 0; i < 7; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                dayWeeks[i] = DayWeek.builder().date(jsonObject1.getString("date"))
                        .timeTable(jsonArray.getJSONObject(i).getString("timeTable")).build();
            }
            log.info("Из запроса получены данные планировщика");
        } catch (JSONException e) {
            log.info("Не удалось получить из запроса данные планировщика, ошибка {}", e.getMessage());
            log.info("Выполняем заполнение планировщика данными");
            for (int i = 0; i < 7; i++) {
                dayWeeks[i] = DayWeek.builder().date(new SimpleDateFormat("dd-MM-yyyy")
                                .format(System.currentTimeMillis() + i * 86400000))
                        .timeTable("").build();
            }
        }
        return dayWeeks;
    }
}
