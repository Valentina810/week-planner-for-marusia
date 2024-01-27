package com.github.valentina810.weekplannerformarusia.storage.session;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
public class SessionStorage { //#todo унифицировать методы, есть дублирующиеся, задокументировать

    private static final Actions ACTIONS_EMPTY = Actions.builder().prevActions(Collections.emptyList()).build();
    private static final ActionsStorage ACTIONS_STORAGE_EMPTY = ActionsStorage.builder().actions(ACTIONS_EMPTY).build();

    @Getter
    private Object session_state;

    public void setActions(Actions actions) {
        session_state = ActionsStorage.builder().actions(actions).build();
    }

    /**
     * Возвращает session_state в виде объекта ActionsStorage
     */
    public ActionsStorage getActionsStorage() {
        return Optional.ofNullable(session_state)
                .map(obj -> new Gson().fromJson(new Gson().toJson(obj), ActionsStorage.class))
                .orElse(ACTIONS_STORAGE_EMPTY);
    }

    /**
     * Очистить список предыдущих активностей
     */
    public void clear() {
        session_state = ACTIONS_STORAGE_EMPTY;
    }

    public void addAction(PrevAction prevAction) {
        getLastPrevAction()
                .ifPresentOrElse(
                        lastPrevAction -> prevAction.setNumber(lastPrevAction.getNumber() + 1),
                        () -> prevAction.setNumber(0)
                );

        setActions(addPrevActionToStorage(prevAction).getActions());
    }

    private ActionsStorage addPrevActionToStorage(PrevAction prevAction) {
        ActionsStorage actionsStorage = getActionsStorage();
        actionsStorage.getActions().getPrevActions().add(prevAction);
        return actionsStorage;
    }

    public Optional<PrevAction> getLastPrevAction() {
        return getActionsStorage().getActions().getPrevActions()
                .stream().max(Comparator.comparingInt(PrevAction::getNumber));
    }

    public List<PrevAction> getPrevActions() {
        return getActionsStorage().getActions().getPrevActions().stream().toList();
    }

    public void calculatePrevActions(Object object) {
        try {
            session_state = Optional.of(object)
                    .filter(obj -> obj instanceof JSONObject)
                    .map(parseObject())
                    .orElse(ACTIONS_STORAGE_EMPTY);
        } catch (Exception e) {
            log.error("Ошибка при преобразовании ActionsStorage из входящего запроса:{}", e.getMessage());
            setActions(ACTIONS_EMPTY);
        }
    }

    private Function<Object, ActionsStorage> parseObject() {
        return obj -> {
            if ("{}".equals(obj.toString())) {
                return ACTIONS_STORAGE_EMPTY;
            }
            return new Gson().fromJson(obj.toString(), ActionsStorage.class);
        };
    }
}
