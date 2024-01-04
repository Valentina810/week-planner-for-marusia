package com.github.valentina810.weekplannerformarusia.storage.session;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
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
@Component
@RequiredArgsConstructor
public class SessionStorage {

    private static final Actions ACTIONS_EMPTY = Actions.builder().prevActions(Collections.EMPTY_LIST).build();

    @Getter
    private Object session_state;

    public void setActions(Actions actions) {
        session_state = ActionsStorage.builder().actions(actions).build();
    }

    /**
     * Очистить список предыдущих активностей
     */
    public void clear() {
        session_state = ActionsStorage.builder()
                .actions(ACTIONS_EMPTY).build();
    }

    public void addAction(PrevAction prevAction) {
        ActionsStorage actionsStorage = getActionsStorage();
        actionsStorage.getActions().getPrevActions().add(prevAction);
        setActions(actionsStorage.getActions());
    }

    public Optional<PrevAction> getLastPrevAction() {
        return getActionsStorage().getActions().getPrevActions()
                .stream().max(Comparator.comparingInt(PrevAction::getNumber));
    }

    /**
     * Возвращает session_state в виде объекта ActionsStorage
     */
    private ActionsStorage getActionsStorage() {
        return session_state != null ? new Gson().fromJson(new Gson().toJson(session_state), ActionsStorage.class) :
                new Gson().fromJson(new Gson().toJson(new Object()), ActionsStorage.class);
    }

    public void calculatePrevActions(Object object) {
        try {
            ActionsStorage actionsStorage = Optional.of(object)
                    .filter(obj -> obj instanceof JSONObject)
                    .map(parseObject())
                    .orElseThrow(() -> new ClassCastException("Невозможно преобразовать объект в JSONObject: " + object));

            session_state = Optional.of(actionsStorage)
                    .filter(storage -> storage.getActions() != null)
                    .orElse(ActionsStorage.builder().actions(ACTIONS_EMPTY).build());

        } catch (Exception e) {
            log.error("Ошибка при преобразовании ActionsStorage из входящего запроса:{}", e.getMessage());
            setActions(ACTIONS_EMPTY);
        }
    }

    private Function<Object, ActionsStorage> parseObject() {
        return obj -> new Gson().fromJson(obj.toString(), ActionsStorage.class);
    }
}
