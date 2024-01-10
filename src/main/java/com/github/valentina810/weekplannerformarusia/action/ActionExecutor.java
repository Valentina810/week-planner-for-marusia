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

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.UNKNOWN;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionExecutor {
    private final CommandLoader commandLoader;
    private final HandlerFactory handlerFactory;
    @Getter
    private final UserResponse userResponse;

    /**
     * Формирует ответ для пользователя в объекте userResponse
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
     * @return - набор параметров для ответа в виде модального объекта
     */
    private ParametersHandler getParametersHandler(UserRequest userRequest) {
        String phrase = getPhrase(userRequest.getRequest().getCommand());
        SimpleHandler handler = getHandler(userRequest.getState().getSession(), phrase);
        handler.getParametersHandler().setUserRequest(userRequest);
        handler.execute();
        return handler.getParametersHandler();
    }

    /**
     * Выбор обработчика в зависимости от наличия предыдущих команд
     *
     * @param requestSessionStorage - данные в хранилище сесии из запроса
     * @param phrase                - фраза пользователя
     * @return - обработчик, рассчитаный на основе входных параметров
     */
    private SimpleHandler getHandler(Object requestSessionStorage, String phrase) {
        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.calculatePrevActions(requestSessionStorage);
        return sessionStorage.getLastPrevAction()
                .map(prevAction -> getHandlerBasedOnPreviousActivity(prevAction, phrase))
                .orElseGet(() -> getMainMenuCommandHandler(phrase));
    }

    private SimpleHandler getMainMenuCommandHandler(String phrase) {
        Command command = commandLoader.get(phrase);
        SimpleHandler handler = handlerFactory.getHandler(command);
        handler.getParametersHandler().setCommand(command);
        return handler;
    }

    private SimpleHandler getHandlerBasedOnPreviousActivity(PrevAction prevAction, String phrase) {
        Command childCommand = commandLoader.get(prevAction.getOperation())
                .getActions().stream()
                .filter(e -> e.getPhrase().equals(phrase))
                .findFirst().orElse(commandLoader.get(UNKNOWN));
        SimpleHandler handler = handlerFactory.getHandler(childCommand);
        handler.getParametersHandler().setCommand(childCommand);
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
        return message != null ?
                message.replaceAll("[^а-яА-Я0-9\\s]", "")
                        .toLowerCase() : "";
    }
}