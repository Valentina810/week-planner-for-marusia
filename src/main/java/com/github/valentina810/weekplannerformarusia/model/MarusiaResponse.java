package com.github.valentina810.weekplannerformarusia.model;

import com.github.valentina810.weekplannerformarusia.action.Action;
import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
    public Action getAction(Object object) {
        String json = new Gson().toJson(object);
        JsonElement jsonElement = new Gson().fromJson(json, JsonElement.class);
        String escapedPhrase = getPhrase(json);
        Action action =
                Action.builder()
                        .sessionStorage(SessionStorage.builder().build())
                        .persistentStorage(PersistentStorage.builder().build())
                        .message(escapedPhrase)
                        .isEndSession(false)
                        .prevAction(SessionStorage.builder().build().getPrevAction())
                        .build();
        action.setSessionStorage(SessionStorage.builder()
                .session_state(jsonElement.getAsJsonObject().getAsJsonObject("state")
                        .getAsJsonObject("session")).build());
        action.setPersistentStorage(PersistentStorage.builder().
                user_state_update(jsonElement.getAsJsonObject().getAsJsonObject("state")
                        .getAsJsonObject("user")).build());
        action.setPrevAction(action.getSessionStorage().getPrevAction());
        return action;
    }

    /**
     * Получить из объекта фразу, которую сказал пользователь
     *
     * @param jsonString - объект
     * @return - фраза
     */
    private static String getPhrase(String jsonString) {
        try {
            return new JSONObject(jsonString)
                    .getJSONObject("request")
                    .getString("original_utterance")
                    .replaceAll("[^а-яА-Я0-9\\s]", "")
                    .toLowerCase();
        } catch (JSONException e) {
            log.info("Возникла ошибка {} при получении фразы из запроса", e.getMessage());
            return "";
        }
    }
}