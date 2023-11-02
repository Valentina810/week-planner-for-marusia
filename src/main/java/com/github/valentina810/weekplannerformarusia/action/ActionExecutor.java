package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.handler.BaseHandler;
import com.github.valentina810.weekplannerformarusia.action.handler.BaseHandlerFactory;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_EVENT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.NONE;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Slf4j
@RequiredArgsConstructor
@Component
public class ActionExecutor {

    private final BaseHandlerFactory baseHandlerFactory;
    @Getter
    private final UserResponse userResponse;

    /**
     * Формирует ответ для пользователя в поле userResponse
     */
    public void createUserResponse(UserRequest userRequest) {
        BaseHandler baseHandlerResponsePhrase =
                baseHandlerFactory.getByBaseHandlerResponsePhraseType(getAction(getPhrase(userRequest.getRequest().getCommand())));
        String message = baseHandlerResponsePhrase.find(userRequest);

        userResponse.setResponse(Response.builder()
                .text(message)
                .tts(message)
                .end_session(false).build());

        userResponse.setSession(Session.builder()
                .user_id(userRequest.getSession().getUser_id())
                .session_id(userRequest.getSession().getSession_id())
                .message_id(userRequest.getSession().getMessage_id())
                .build());

        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.getPrevAction();
        userResponse.setSession_state(sessionStorage.getSession_state());

        PersistentStorage persistentStorage = new PersistentStorage();
        persistentStorage.getWeekEvents(userRequest.getState().getUser());
        userResponse.setUser_state_update(persistentStorage.getUser_state_update());

        userResponse.setVersion(userRequest.getVersion());
    }

    /**
     * Получить из объекта фразу, которую сказал пользователь
     * #todo - позже будет анализ по токенам
     *
     * @param message - объект
     * @return - фраза
     */
    private String getPhrase(String message) {
        return message
                .replaceAll("[^а-яА-Я0-9\\s]", "")
                .toLowerCase();
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