package com.github.valentina810.weekplannerformarusia.model;

import com.github.valentina810.weekplannerformarusia.action.Action;
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
        return formRequest(new MarusiaResponse()
                        .getAction(object)
                        .reply(),
                getRequestAttributes(object));
    }

    /**
     * Сформировать ответ
     *
     * @param action             - ответ на фразу
     * @param requiredAttributes - обязательные атрибуты для формирования запроса,
     *                           полученные из ответа
     * @return - сформированный запрос
     */
    private MarusiaRequest formRequest(final Action action,
                                       final RequiredAttributes requiredAttributes) {
        return MarusiaRequest.builder()
                .response(Response.builder()
                        .text(action.getMessage())
                        .tts(action.getMessage())
                        .end_session(action.getIsEndSession()).build())
                .session(Session.builder()
                        .user_id(requiredAttributes.getUserId())
                        .session_id(requiredAttributes.getSessionId())
                        .message_id(requiredAttributes.getMessageId()).build())
                .version(requiredAttributes.getVersion())
                .user_state_update(action.getPersistentStorage().getUser_state_update())
                .session_state(action.getSessionStorage().getSession_state())
                .build();
    }

    /**
     * Получить из ответа обязательные для передачи атрибуты
     */
    private RequiredAttributes getRequestAttributes(final Object object) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
            return RequiredAttributes.builder()
                    .userId(jsonObject.getJSONObject("session").getString("user_id"))
                    .sessionId(jsonObject.getJSONObject("session").getString("session_id"))
                    .messageId(jsonObject.getJSONObject("session").getInt("message_id"))
                    .version(jsonObject.getString("version")).build();
        } catch (JSONException e) {
            log.info("Возникла ошибка {} при получении обязательных атрибутов из запроса", e.getMessage());
            return null;
        }
    }
}