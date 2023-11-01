package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.handler.BaseHandlerResponsePhrase;
import com.github.valentina810.weekplannerformarusia.action.handler.Handlers;
import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_EVENT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.NONE;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Builder
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    @Setter
    private SessionStorage sessionStorage;
    @Setter
    private PersistentStorage persistentStorage;
    private String message;
    @Setter
    private PrevAction prevAction;
    private Boolean isEndSession;
    private Map<TypeAction, BaseHandlerResponsePhrase> handlers;

    /**
     * Получение из входного запроса данных,
     * необходимых для формирования ответа
     *
     * @param object -  тело входного запроса
     * для формирования ответа
     */
    public void createAction(Object object) {
        String json = new Gson().toJson(object);
        JsonElement jsonElement = new Gson().fromJson(json, JsonElement.class);
        String escapedPhrase = getPhrase(json);
        sessionStorage = SessionStorage.builder().build();
        persistentStorage = PersistentStorage.builder().build();
        message = escapedPhrase;
        isEndSession = false;
        prevAction = SessionStorage.builder().build().getPrevAction();
        sessionStorage = SessionStorage.builder()
                .session_state(jsonElement.getAsJsonObject().getAsJsonObject("state")
                        .getAsJsonObject("session")).build();
        persistentStorage = PersistentStorage.builder().
                user_state_update(jsonElement.getAsJsonObject().getAsJsonObject("state")
                        .getAsJsonObject("user")).build();
        prevAction = sessionStorage.getPrevAction();
    }

    /**
     * Получить из объекта фразу, которую сказал пользователь
     *
     * @param jsonString - объект
     * @return - фраза
     */
    private String getPhrase(String jsonString) {
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

    /**
     * На основе данных формируем ответ для пользователя
     * @return
     */
    public Action reply() {
        handlers = new Handlers().getBaseHandlers();
        BaseHandlerResponsePhrase baseHandlerResponsePhrase = handlers.get(getAction(message));
        if (baseHandlerResponsePhrase != null) {
            message = baseHandlerResponsePhrase.find(this);
            persistentStorage.getWeekEvents();
            isEndSession = false;
        } else {
            throw new RuntimeException("Отсутствует реализация для " + getAction(message));
        }
        return this;
    }

    /**
     * Возвращает тип активности, который удалось выделить из фразы пользователя
     *
     * @param message - фраза
     * @return - выделенная активность
     */
    private TypeAction getAction(String message) {
        return switch (message) {
            case "план на неделю" -> WEEKLY_PLAN;
            case "план на сегодня" -> TODAY_PLAN;
            case "план на завтра" -> TOMORROW_PLAN;
            case "добавь событие" -> ADD_EVENT;
            case "справка" -> HELP;
            case "выход" -> EXIT;
            default -> NONE;
        };
    }
}