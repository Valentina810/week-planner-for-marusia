package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.executor.ExecutorFactory;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.dto.Token;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.persistent.WeekStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.github.valentina810.weekplannerformarusia.util.Formatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.UNKNOWN;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionExecutor {
    private final ExecutorFactory executorFactory;
    private final TokenLoader tokenLoader;

    /**
     * Формирует ответ для пользователя в объекте userResponse
     */
    public UserResponse createUserResponse(Object object) {
        UserRequest userRequest = new UserRequest();
        userRequest.fillUserRequest(object);
        log.info("Получен запрос {}", userRequest);

        ResponseParameters respParam = getResponseParameters(userRequest);

        UserResponse userResponse = new UserResponse();
        userResponse.setResponse(Response.builder()
                .text(respParam.getRespPhrase())
                .tts(respParam.getRespPhrase())
                .end_session(respParam.getIsEndSession())
                .build());

        userResponse.setSession(Session.builder()
                .user_id(userRequest.getSession().getUser_id())
                .session_id(userRequest.getSession().getSession_id())
                .message_id(userRequest.getSession().getMessage_id())
                .build());

        setSession_state(userResponse, respParam);

        WeekStorage userStateUpdate = respParam.getPersistentStorage().getUser_state_update();
        userResponse.setUser_state_update(userStateUpdate);

        userResponse.setVersion(userRequest.getVersion());

        log.info("Сформирован ответ {}", userResponse);
        log.info("------------------------------------------------------------------------------------------------------");
        return userResponse;
    }

    private static void setSession_state(UserResponse userResponse, ResponseParameters respParam) {
        userResponse.setSession_state(
                Optional.ofNullable(respParam.getSessionStorage())
                        .map(SessionStorage::getSession_state)
                        .orElse(null)
        );
    }

    private ResponseParameters getResponseParameters(final UserRequest userRequest) {

        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.setActionsInSessionState(userRequest.getState().getSession());

        PersistentStorage persistentStorage = new PersistentStorage();
        persistentStorage.setWeekStorage(userRequest.getState().getUser());
        if (userRequest.getSession().getMessage_id() == 0) {
            persistentStorage.removeObsoleteEvents(Formatter.getCurrentDateForTimeZone.apply(userRequest.getMeta().getTimezone()));
        }

        Actions actions = sessionStorage.getActions();
        String phrase = userRequest.getRequest().getCommand();
        TypeAction typeAction = defineCommand(actions, phrase);
        log.info("Получить тип активности на основании actions={}, phrase={}, typeAction={}",
                actions, phrase, typeAction);

        return executorFactory.getExecutor(typeAction)
                .getResponseParameters(ExecutorParameter.builder()
                        .typeAction(typeAction)
                        .phrase(phrase)
                        .zoneId(userRequest.getMeta().getTimezone())
                        .sessionStorage(sessionStorage)
                        .persistentStorage(persistentStorage).build());
    }

    /**
     * Получить из фразы, которую сказал пользователь, код активности
     *
     * @param phrase - фраза пользователя
     * @return код активности
     */
    private TypeAction defineCommand(Actions actions, String phrase) {
        List<PrevAction> prevActions = actions.getPrevActions();
        List<Token> tokens = tokenLoader.getTokens();
        return prevActions.isEmpty() ?
                determineCommandBasedOnPhrase(phrase, tokens) :
                determineCommandBasedOnPrevActions(phrase, prevActions, tokens);
    }

    /**
     * Определить команду на основе фразы
     */
    private static TypeAction determineCommandBasedOnPhrase(String phrase, List<Token> tokens) {
        Optional<TypeAction> result = tokens.stream()
                .filter(token -> token.getTokens().stream()
                        .anyMatch(phraseTokens -> phraseTokens.getPhrase().stream()
                                .allMatch(phrase::contains)))
                .map(Token::getTypeAction)
                .findFirst();

        return result.orElse(UNKNOWN);
    }

    /**
     * Определить команду на основе предыдущих активностей
     */
    private static TypeAction determineCommandBasedOnPrevActions(String phrase, List<PrevAction> prevActions, List<Token> tokens) {
        TypeAction operation = prevActions.stream()
                .max(comparingInt(PrevAction::getNumber))
                .get()
                .getOperation();
        List<Command> commandsByPrevOperation =
                CommandLoader.findCommandsByPrevOperation(operation);
        if (commandsByPrevOperation.size() == 1) {
            return commandsByPrevOperation.getFirst().getOperation();
        } else {
            return determineCommandBasedOnPhrase(phrase, tokens.stream()
                    .filter(e -> commandsByPrevOperation.stream()
                            .map(Command::getOperation)
                            .collect(toSet())
                            .contains(e.getTypeAction()))
                    .toList());
        }
    }
}