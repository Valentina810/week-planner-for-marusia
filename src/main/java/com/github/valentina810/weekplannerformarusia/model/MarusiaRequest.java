package com.github.valentina810.weekplannerformarusia.model;

import com.github.valentina810.weekplannerformarusia.action.BaseAction;
import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
    private SessionStorage session_state;
    private PersistentStorage user_state_update;

    /**
     * Заготовка для формирования ответа + положить user_state_update и session_state
     *
     * @return - ответ для Маруси
     */
    public MarusiaRequest getRequest(BaseAction baseAction) {
        String message = baseAction.getMessage();
        log.info("Получена команда {}", message);
        switch (message) {
            case "план на неделю":
                break;
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

//        MarusiaRequest marusiaRequest;
//        marusiaRequest = MarusiaRequest.builder()
//                .response(Response.builder()
//                        .text(responsePhrase)
//                        .tts(responsePhrase)
//                        .end_session(false).build())
//                .session(Session.builder()
//                        .user_id(jsonObject.getJSONObject("session").getString("user_id"))
//                        .session_id(jsonObject.getJSONObject("session").getString("session_id"))
//                        .message_id(jsonObject.getJSONObject("session").getInt("message_id")).build())
//                .version(jsonObject.getString("version"))
//                .user_state_update(PersistentStorage.builder().build())
//                .build();
        return new MarusiaRequest();
    }
}