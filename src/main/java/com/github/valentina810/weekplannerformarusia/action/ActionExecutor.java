package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.handler.BaseHandler;
import com.github.valentina810.weekplannerformarusia.action.handler.HandlerFactory;
import com.github.valentina810.weekplannerformarusia.action.handler.LoadCommand;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ActionExecutor {
    private final Loader loader;
    private final HandlerFactory handlerFactory;
    @Getter
    private final UserResponse userResponse;

    /**
     * Формирует ответ для пользователя в поле userResponse
     */
    public void createUserResponse(UserRequest userRequest) {
        String phrase = getPhrase(userRequest.getRequest().getCommand());
        LoadCommand loadCommand = loader.get(phrase);
        BaseHandler handler = handlerFactory.getByBaseHandlerResponsePhraseType(loadCommand.getOperation());
        handler.getParametersHandler().setLoadCommand(loadCommand);
        handler.getParametersHandler().setUserRequest(userRequest);
        handler.getAction(userRequest);

        userResponse.setResponse(Response.builder()
                .text(handler.getParametersHandler().getRespPhrase())
                .tts(handler.getParametersHandler().getRespPhrase())
                .end_session(handler.getParametersHandler().getIsEndSession())
                .build());

        userResponse.setSession(Session.builder()
                .user_id(userRequest.getSession().getUser_id())
                .session_id(userRequest.getSession().getSession_id())
                .message_id(userRequest.getSession().getMessage_id())
                .build());

        userResponse.setSession_state(handler.getParametersHandler().getSessionStorage().getSession_state());

        userResponse.setUser_state_update(handler.getParametersHandler().getPersistentStorage().getUser_state_update());

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
}