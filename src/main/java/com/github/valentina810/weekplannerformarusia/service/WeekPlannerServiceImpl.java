package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.action.Event;
import com.github.valentina810.weekplannerformarusia.action.Exit;
import com.github.valentina810.weekplannerformarusia.action.Faq;
import com.github.valentina810.weekplannerformarusia.action.PlanForTomorrow;
import com.github.valentina810.weekplannerformarusia.action.PlanToday;
import com.github.valentina810.weekplannerformarusia.action.Unknown;
import com.github.valentina810.weekplannerformarusia.action.WeeklyPlan;
import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.model.MarusiaResponse;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class WeekPlannerServiceImpl implements WeekPlannerService {

    private PersistentStorage persistentStorage;

    @Override
    public ResponseEntity<?> getResponse(Object object) {
        String body = new Gson().toJson(getResponseObject(object));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        log.info("Сформирован ответ {}", body);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    /**
     * Получить из входного объекта данные для ответа
     *
     * @param object -  входной запрос
     * @return - ответ
     */
    private MarusiaResponse getResponseObject(Object object) {
        MarusiaResponse marusiaResponse;
        try {
            String jsonString = new Gson().toJson(object);
            JSONObject jsonObject = new JSONObject(jsonString);
            String escapedPhrase = new JSONObject(jsonString)
                    .getJSONObject("request")
                    .getString("original_utterance")
                    .replaceAll("[^а-яА-Я0-9\\s]", "")
                    .toLowerCase();
            String responsePhrase;
            log.info("Получена команда {}", escapedPhrase);
            responsePhrase = switch (escapedPhrase) {
                case "план на неделю" -> WeeklyPlan.action();
                case "план на сегодня" -> PlanToday.action();
                case "план на завтра" -> PlanForTomorrow.action();
                case "добавь событие" -> Event.addEvent();
                case "справка" -> Faq.action();
                case "выход" -> Exit.action();
                default -> Unknown.action();
            };
            marusiaResponse = MarusiaResponse.getMarusiaResponse(responsePhrase, jsonObject, persistentStorage.getDayWeeks(jsonObject));
        } catch (JSONException e) {
            log.info("Возникла ошибка в процессе распарсивания запроса {}", e.getMessage());
            return null;
        }
        return marusiaResponse;
    }
}