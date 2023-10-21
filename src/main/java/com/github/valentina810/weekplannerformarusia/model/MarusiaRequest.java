package com.github.valentina810.weekplannerformarusia.model;

import com.github.valentina810.weekplannerformarusia.action.BaseAction;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Builder
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class MarusiaRequest {
    private Response response;
    private Session session;
    private String version;
    private Object session_state;
    private Object user_state_update;
    private RequiredAttributes requiredAttributes;

    /**
     * Заготовка для формирования ответа + положить user_state_update и session_state
     *
     * @return - ответ для Маруси
     */
    public MarusiaRequest getRequest(Object object) {
        BaseAction baseAction = new MarusiaResponse().getAction(object);
        String message = baseAction.getMessage();
        requiredAttributes = getRequestAttributes(object);
        log.info("Получена команда {}", message);
        if (baseAction.getSessionStorage().getPrevAction().getTypeAction()
                .equals(TypeAction.NONE)) {
            switch (message) {
                case "план на неделю":
                    return formRequest(baseAction.replyWeeklyPlan(), requiredAttributes);
                case "план на сегодня":
                    break;
                case "план на завтра":
                    break;
                case "добавь событие":
                    break;
                case "справка":
                    break;
                case "выход":
                    break;
                default:
            }
        } else {
            //формируем запрос с учетом переданных ранее активностей
        }
        return new MarusiaRequest();
    }

    /**
     * Сформировать ответ
     *
     * @param baseAction         - ответ на фразу
     * @param requiredAttributes - обязательные атрибуты для формирования запроса,
     *                           полученные из ответа
     * @return - сформированный запрос
     */
    private MarusiaRequest formRequest(final BaseAction baseAction, final RequiredAttributes requiredAttributes) {
        return MarusiaRequest.builder()
                .response(Response.builder()
                        .text(baseAction.getMessage())
                        .tts(baseAction.getMessage())
                        .end_session(baseAction.getIsEndSession()).build())
                .session(Session.builder()
                        .user_id(requiredAttributes.getUserId())
                        .session_id(requiredAttributes.getSessionId())
                        .message_id(requiredAttributes.getMessageId()).build())
                .version(requiredAttributes.getVersion())
                .user_state_update(baseAction.getPersistentStorage().getUser_state_update())
                .session_state(baseAction.getSessionStorage().getSession_state())
                .build();
    }

    /**
     * Получить из ответа обязательные для передачи атрибуты
     */
    private RequiredAttributes getRequestAttributes(final Object object) {
        String jsonString = new Gson().toJson(object);
        JSONObject jsonObject = new JSONObject(jsonString);
        return RequiredAttributes.builder()
                .userId(jsonObject.getJSONObject("session").getString("user_id"))
                .sessionId(jsonObject.getJSONObject("session").getString("session_id"))
                .messageId(jsonObject.getJSONObject("session").getInt("message_id"))
                .version(jsonObject.getString("version")).build();
    }
}