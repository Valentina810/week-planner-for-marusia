package com.github.valentina810.weekplannerformarusia.storage.session;


import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

/**
 * Хранилище, которое хранит данные в контексте сессии
 * поле session_state - передаем, state.session - получаем
 * лимит размера json-объекта session_state — 5 Кбайт
 * состояние теряется в таких случаях:
 * - пользователь выходит из скилла;
 * - скилл сам явно завершает работу, передав "end_session": true;
 * - выход происходит по таймауту, когда пользователь не отвечает некоторое время (1 минуту).
 * https://dev.vk.com/ru/marusia/session-state
 */

@Slf4j
@Builder
@AllArgsConstructor
public class SessionStorage {

    private ActionsStorage actionsStorage;

    public SessionStorage() {
        SessionStorage.builder()
                .actionsStorage(ActionsStorage.builder()
                        .actions(Actions.builder()
                                .prevActions(new ArrayList<>())
                                .build())
                        .build())
                .build();
    }

    public ActionsStorage getSession_state() {
        return actionsStorage;
    }

    public Actions getActions() {
        return getActionsStorage().getActions();
    }

    public void addPrevAction(PrevAction prevAction) {
        if (actionsStorage == null) {
            actionsStorage = ActionsStorage.builder().actions(Actions.builder().prevActions(new ArrayList<>()).build()).build();
        }
        Actions actions = actionsStorage.getActions();
        actions.addAction(prevAction);
        actionsStorage = ActionsStorage.builder().actions(actions).build();
    }

    public void setActionsInSessionState(Object object) {
        try {
            actionsStorage = Optional.of(object)
                    .filter(obj -> obj instanceof JSONObject)
                    .map(parseObject())
                    .orElse(null);
        } catch (Exception e) {
            log.error("Ошибка при преобразовании ActionsStorage из входящего запроса:{}", e.getMessage());
            actionsStorage = ActionsStorage.builder().actions(Actions.builder().prevActions(new ArrayList<>()).build()).build();
        }
    }

    /**
     * Возвращает session_state в виде объекта ActionsStorage
     */
    private ActionsStorage getActionsStorage() {
        return Optional.ofNullable(actionsStorage)
                .map(obj -> new Gson().fromJson(new Gson().toJson(obj), ActionsStorage.class))
                .orElse(ActionsStorage.builder().build());
    }

    private Function<Object, ActionsStorage> parseObject() {
        return obj -> {
            if ("{}".equals(obj.toString())) {
                return null;
            }
            return new Gson().fromJson(obj.toString(), ActionsStorage.class);
        };
    }
}
