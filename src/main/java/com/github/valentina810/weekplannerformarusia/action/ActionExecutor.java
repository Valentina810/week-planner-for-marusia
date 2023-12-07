package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.handler.BaseHandler;
import com.github.valentina810.weekplannerformarusia.action.handler.BaseHandlerFactory;
import com.github.valentina810.weekplannerformarusia.action.handler.helphandlerchild.CommandHelpHandler;
import com.github.valentina810.weekplannerformarusia.action.phrase.BasePhraseFactory;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_EVENT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;

@Slf4j
@RequiredArgsConstructor
@Component
public class ActionExecutor {

    private final BaseHandlerFactory baseHandlerFactory;
    private final BasePhraseFactory basePhraseFactory;
    @Getter
    private final UserResponse userResponse;

    /**
     * Формирует ответ для пользователя в поле userResponse
     */
    public void createUserResponse(UserRequest userRequest) {
        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.getPrevActions(userRequest.getState().getSession());
        Actions actions = sessionStorage.getActions();
        if (!actions.getActions().isEmpty()) {
            switch (actions.getActions().last().getTypeAction()) {
                case HELP:
                    new CommandHelpHandler().find(getPhrase(userRequest.getRequest().getCommand()));
                    break;
                case ADD_EVENT:

                    break;
            }
        } else {
            BaseHandler handler =
                    baseHandlerFactory.getByBaseHandlerResponsePhraseType(getAction(getPhrase(
                            userRequest.getRequest().getCommand())));
            String message = handler.find(userRequest);

            userResponse.setResponse(Response.builder()
                    .text(message)
                    .tts(message)
                    .end_session(handler.getType().equals(EXIT)).build());

            userResponse.setSession(Session.builder()
                    .user_id(userRequest.getSession().getUser_id())
                    .session_id(userRequest.getSession().getSession_id())
                    .message_id(userRequest.getSession().getMessage_id())
                    .build());

            if (handler.getType().equals(ADD_EVENT) ||
                    handler.getType().equals(HELP)) {
                sessionStorage.getActions().getActions().add(
                        PrevAction.builder()
                                .number(sessionStorage.getActions().getActions().size())
                                .typeAction(handler.getType())
                                .valueAction("")
                                .build()
                );
            }
            userResponse.setSession_state(sessionStorage.getSession_state());

            PersistentStorage persistentStorage = new PersistentStorage();
            persistentStorage.getWeekEvents(userRequest.getState().getUser());
            userResponse.setUser_state_update(persistentStorage.getUser_state_update());

            userResponse.setVersion(userRequest.getVersion());
        }
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
     * @param phrase - фраза
     * @return - выделенная активность
     */
    private TypeAction getAction(String phrase) {
        return basePhraseFactory.getBasePhrase(phrase);
    }
}