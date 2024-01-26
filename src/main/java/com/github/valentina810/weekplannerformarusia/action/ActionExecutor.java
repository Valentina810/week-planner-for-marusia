package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.action.handler.HandlerFactory;
import com.github.valentina810.weekplannerformarusia.action.handler.composite.BaseCompositeExecutor;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.dto.Token;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionExecutor {
    private final HandlerFactory handlerFactory;
    @Getter
    private final UserResponse userResponse;

    /**
     * Формирует ответ для пользователя в объекте userResponse
     */
    public void createUserResponse(UserRequest userRequest) {
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
    }

    private ResponseParameters getResponseParameters(UserRequest userRequest) {

        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.calculatePrevActions(userRequest.getState().getSession());

        PersistentStorage persistentStorage = new PersistentStorage();
        persistentStorage.getWeekEvents(userRequest.getState().getUser());

        List<PrevAction> prevActions = sessionStorage.getPrevActions();
        String phrase = userRequest.getRequest().getCommand();
        TypeAction typeAction = defineCommand(prevActions, phrase);

        return getHandler(typeAction)
                .getResponseParameters(ExecutorParameter.builder()
                        .phrase(phrase)
                        .sessionStorage(sessionStorage)
                        .persistentStorage(persistentStorage).build());
    }

    private BaseCompositeExecutor getHandler(TypeAction typeAction) {
        return handlerFactory.getHandler(typeAction);
    }

    /**
     * Получить из фразы, которую сказал пользователь, код активности
     *
     * @param phrase - фраза пользователя
     * @return код активности
     */
    private TypeAction defineCommand(List<PrevAction> prevActions, String phrase) {
//        if (prevActions.size() > 0) {
//            return EXIT;
//            //определяем команду по предыдущей активности
//        } else { //ищем фразу по токенам
//            return loadCommand().stream()
//                    .filter(token -> token.getTokens().stream()
//                            .anyMatch(phraseTokens -> phraseTokens.getPhrase().stream()
//                                    .allMatch(e -> e.contains(phrase))))
//                    .map(Token::getOperation)
//                    .findFirst()
//                    .orElse(UNKNOWN);
//        }
        return EXIT;
    }

    public List<Token> loadCommand() {
        return FileReader.loadJsonFromFile("tokens.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Token.class))
                .collect(Collectors.toList());
    }
}