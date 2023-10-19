package com.github.valentina810.weekplannerformarusia.model;

import com.github.valentina810.weekplannerformarusia.action.BaseAction;
import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MarusiaResponse {
    /**
     * Получение из входного запроса данных,
     * необходимых для формирования ответа
     *
     * @param object -  тело входного запроса
     * @return - сущность активность, которая содержит необходимые свойства
     * для формирования ответа
     */
    public BaseAction getAction(Object object) {
        try {
            String jsonString = new Gson().toJson(object);
            JSONObject jsonObject = new JSONObject(jsonString);
            String escapedPhrase = getPhrase(jsonString);
            return BaseAction.builder()
                    .sessionStorage(SessionStorage.builder()
                            .session_state(jsonObject.getJSONObject("state").getJSONObject("session")).build())
                    .persistentStorage(PersistentStorage.builder().
                            user_state_update(jsonObject.getJSONObject("state").getJSONObject("user")).build())
                    .message(escapedPhrase)
                    .build();
        } catch (JSONException e) {
            log.info("Возникла ошибка в процессе распарсивания запроса {}", e.getMessage());
            return null;
        }
    }

    /**
     * Получить из объекта фразу, которую сказал пользователь
     * @param jsonString - объект
     * @return - фраза
     */
    private static String getPhrase(String jsonString) {
        return new JSONObject(jsonString)
                .getJSONObject("request")
                .getString("original_utterance")
                .replaceAll("[^а-яА-Я0-9\\s]", "")
                .toLowerCase();
    }
}