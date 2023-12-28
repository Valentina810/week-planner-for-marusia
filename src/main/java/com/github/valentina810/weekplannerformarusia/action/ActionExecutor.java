package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.handler.HandlerFactory;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import com.github.valentina810.weekplannerformarusia.action.handler.template.SimpleHandler;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.UNKNOWN;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionExecutor {
    private final Loader loader;
    private final HandlerFactory handlerFactory;
    @Getter
    private final UserResponse userResponse;

    /**
     * Формирует ответ для пользователя в поле userResponse
     */
    public void createUserResponse(UserRequest userRequest) {
        ParametersHandler parametersHandler = getParametersHandler(userRequest);

        userResponse.setResponse(Response.builder()
                .text(parametersHandler.getRespPhrase())
                .tts(parametersHandler.getRespPhrase())
                .end_session(parametersHandler.getIsEndSession())
                .build());

        userResponse.setSession(Session.builder()
                .user_id(userRequest.getSession().getUser_id())
                .session_id(userRequest.getSession().getSession_id())
                .message_id(userRequest.getSession().getMessage_id())
                .build());

        userResponse.setSession_state(parametersHandler.getSessionStorage().getSession_state());

        userResponse.setUser_state_update(parametersHandler.getPersistentStorage().getUser_state_update());

        userResponse.setVersion(userRequest.getVersion());
    }

    /**
     * Получить ответ в формате набора параметров
     *
     * @param userRequest - запрос
     * @return - набор парамтеров для ответа в виде модального объекта
     */
    private ParametersHandler getParametersHandler(UserRequest userRequest) {
        String phrase = getPhrase(userRequest.getRequest().getCommand());
        SessionStorage sessionStorage = new SessionStorage();
        Object session = userRequest.getState().getSession();
        sessionStorage.getPrevActions(session);
        List<PrevAction> prevActions = sessionStorage.getActionsStorage().getActions().getPrevActions();
        SimpleHandler handler = getHandler(prevActions, phrase);
        handler.getParametersHandler().setUserRequest(userRequest);
        handler.execute();
        return handler.getParametersHandler();
    }


    private SimpleHandler getHandler(List<PrevAction> prevActions, String phrase) {
        return prevActions.isEmpty() ? getParametersHandler(phrase) : getParametersHandler(prevActions, phrase);
    }

    private SimpleHandler getParametersHandler(String phrase) {
        LoadCommand loadCommand = loader.get(phrase);
        SimpleHandler handler = handlerFactory.getHandler(loadCommand);
        handler.getParametersHandler().setLoadCommand(loadCommand);
        return handler;
    }

    private SimpleHandler getParametersHandler(List<PrevAction> prevActions, String phrase) {
        PrevAction prevAction = prevActions
                .stream().max(Comparator.comparingInt(PrevAction::getNumber))
                .orElseThrow(() -> new RuntimeException("Список prevActions пуст!"));

        LoadCommand loadCommand = loader.get(prevAction.getOperation());
        LoadCommand childLoadCommand = loadCommand.getActions().stream()
                .filter(e -> e.getPhrase().equals(phrase)).findFirst().orElse(loader.get(UNKNOWN));

        SimpleHandler handler = handlerFactory.getHandler(childLoadCommand);
        handler.getParametersHandler().setLoadCommand(childLoadCommand);
        return handler;
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