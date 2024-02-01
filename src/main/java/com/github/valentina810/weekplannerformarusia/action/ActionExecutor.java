package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.action.handler.HandlerFactory;
import com.github.valentina810.weekplannerformarusia.action.handler.composite.BaseExecutor;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.dto.Token;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.UNKNOWN;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionExecutor {
    private final HandlerFactory handlerFactory;
    @Getter
    private UserResponse userResponse = new UserResponse();

    /**
     * Формирует ответ для пользователя в объекте userResponse
     */
    public void createUserResponse(UserRequest userRequest) {
        log.info("Получен запрос {}", userRequest);

        ResponseParameters respParam = getResponseParameters(userRequest);

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

        userResponse.setSession_state(respParam.getSessionStorage().getSession_state());

        userResponse.setUser_state_update(respParam.getPersistentStorage().getUser_state_update());

        userResponse.setVersion(userRequest.getVersion());

        log.info("Сформирован ответ {}", userResponse);
    }

    private ResponseParameters getResponseParameters(UserRequest userRequest) {

        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.setActionsInSessionState(userRequest.getState().getSession());

        PersistentStorage persistentStorage = new PersistentStorage();
        persistentStorage.setWeekStorage(userRequest.getState().getUser());

        Actions actions = sessionStorage.getActions();
        String phrase = userRequest.getRequest().getCommand();
        TypeAction typeAction = defineCommand(actions, phrase);
        log.info("Получить тип активности на основании actions={}, phrase={}, typeAction={}",
                actions, phrase, typeAction);

        BaseExecutor handler = getHandler(typeAction);
        return handler
                .getResponseParameters(ExecutorParameter.builder()
                        .typeAction(typeAction)
                        .phrase(phrase)
                        .sessionStorage(sessionStorage)
                        .persistentStorage(persistentStorage).build());
    }

    private BaseExecutor getHandler(TypeAction typeAction) {
        return handlerFactory.getHandler(typeAction);
    }

    /**
     * Получить из фразы, которую сказал пользователь, код активности
     *
     * @param phrase - фраза пользователя
     * @return код активности
     */
    private TypeAction defineCommand(Actions actions, String phrase) {//#todo разбить не несколько методов
        List<PrevAction> prevActions = actions.getPrevActions();
        List<Token> tokens = loadCommand();
        if (!prevActions.isEmpty()) { //есть предыдущая активность, по ней определяем какая может быть следующей
            //собираем все команды у которых prevOperation=предыдущей активности с максимальным номером из массива prevActions
            PrevAction prevAction = prevActions.stream().max(Comparator.comparingInt(PrevAction::getNumber)).get();
            //команды нужно перебрать рекусрсивно, так как текущая команда может быть вложенной
            TypeAction operation = prevAction.getOperation();
            List<Command> commandsByPrevOperation =
                    CommandLoader.findCommandsByPrevOperation(operation);
            if (commandsByPrevOperation.size() == 1) {
                return commandsByPrevOperation.get(0).getOperation();
            } else {//иначе нужно подключать поиск по фразе, но только для вложенных команд
                Set<TypeAction> set = commandsByPrevOperation.stream().map(Command::getOperation).collect(Collectors.toSet());
                List<Token> collect = tokens.stream().filter(e -> set.contains(e.getTypeAction())).toList();
                Stream<Token> tokenStream = collect.stream()
                        .filter(token -> token.getTokens().stream()
                                .anyMatch(phraseTokens -> phraseTokens.getPhrase().stream()
                                        .allMatch(e -> phrase.contains(e))));
                Set<Token> collect1 = tokenStream.collect(Collectors.toSet());
                if (!collect1.isEmpty()) {
                    return collect.stream().findFirst().get().getTypeAction();
                } else return UNKNOWN;
            }
        } else { //ищем фразу по токенам
            Stream<Token> tokenStream = tokens.stream()
                    .filter(token -> token.getTokens().stream()
                            .anyMatch(phraseTokens -> phraseTokens.getPhrase().stream()
                                    .allMatch(e -> phrase.contains(e))));
            Set<Token> collect = tokenStream.collect(Collectors.toSet());
            if (!collect.isEmpty()) {
                return collect.stream().findFirst().get().getTypeAction();
            } else return UNKNOWN;
        }
    }

    public List<Token> loadCommand() {//#todo загружать один раз при старте сервиса!
        log.info("Загрузка токенов из файла tokens.json");
        return FileReader.loadJsonFromFile("tokens.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Token.class))
                .collect(Collectors.toList());
    }
}