package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.action.handler.HandlerFactory;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import com.github.valentina810.weekplannerformarusia.action.handler.template.SimpleHandler;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.calculatePrevActions(userRequest.getState().getSession());
        List<PrevAction> prevActions = sessionStorage.getPrevActions();

        TypeAction requestSessionStorage = defineCommand(prevActions, userRequest.getRequest().getCommand());
        SimpleHandler handler = getHandler(requestSessionStorage);
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
     * Получить из фразы, которую сказал пользователь, код активности
     *
     * @param phrase - фраза пользователя
     * @return код активности
     */
    private TypeAction defineCommand(List<PrevAction> prevActions, String phrase) {
        if (prevActions.size() > 0) {
            //определяем команду по предыдущей активности
        } else { //ищем фразу по токенам
            return loadCommand().stream()
                    .filter(token -> token.getTokens().stream()
                            .anyMatch(phraseTokens -> phraseTokens.getPhrase().stream()
                                    .allMatch(e -> e.contains(phrase))))
                    .map(Token::getOperation)
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }

    public List<Token> loadCommand() {
        return FileReader.loadJsonFromFile("tokens.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Token.class))
                .collect(Collectors.toList());
    }
}