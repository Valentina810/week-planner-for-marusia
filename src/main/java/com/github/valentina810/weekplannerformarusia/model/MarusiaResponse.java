package com.github.valentina810.weekplannerformarusia.model;

import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Builder
@Getter
@Setter
public class MarusiaResponse {
    private Response response;
    private Session session;
    private String version;
    private SessionStorage session_state;
    private PersistentStorage user_state_update;

    /**
     * Заготовка для формирования ответа + положить user_state_update и session_state
     *
     * @param responsePhrase
     * @param jsonObject
     * @param dayWeeks
     * @return - ответ для Маруси
     */
    public static MarusiaResponse getMarusiaResponse(String responsePhrase, JSONObject jsonObject, DayWeek[] dayWeeks) {
        MarusiaResponse marusiaResponse;
        marusiaResponse = MarusiaResponse.builder()
                .response(Response.builder()
                        .text(responsePhrase)
                        .tts(responsePhrase)
                        .end_session(false).build())
                .session(Session.builder()
                        .user_id(jsonObject.getJSONObject("session").getString("user_id"))
                        .session_id(jsonObject.getJSONObject("session").getString("session_id"))
                        .message_id(jsonObject.getJSONObject("session").getInt("message_id")).build())
                .version(jsonObject.getString("version"))
                .user_state_update(PersistentStorage.builder().data(dayWeeks).build())
                .build();
        return marusiaResponse;
    }
}