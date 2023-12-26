package com.github.valentina810.weekplannerformarusia.storage.session;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Collections;

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

    @Getter
    private Object session_state;

    public void setSession_state(Object session_state) {
        getPrevActions(session_state);
    }

    /**
     * Возвращает session_state в виде объекта ActionsStorage
     */
    public ActionsStorage getActionsStorage() {
        return new Gson().fromJson(new Gson().toJson(session_state), ActionsStorage.class);
    }

    public void setActions(Actions actions) {
        session_state = ActionsStorage.builder().actions(actions).build();
    }

    /**
     * Очистить список предыдущих активностей
     */
    public void clear() {
        session_state = ActionsStorage.builder()
                .actions(Actions.builder()
                        .prevActions(Collections.EMPTY_LIST).build()).build();
    }

    public void addAction(PrevAction prevAction) {
        ActionsStorage actionsStorage = getActionsStorage();
        actionsStorage.getActions().getPrevActions().add(prevAction);
        setActions(actionsStorage.getActions());
    }

    public void getPrevActions(Object object) {
        try {
            JSONObject jsonObject = (JSONObject) object;
            ActionsStorage actionsStorage = new Gson().fromJson(jsonObject.toString(), ActionsStorage.class);
            if (actionsStorage.getActions() != null) {
                session_state = actionsStorage;
            } else session_state = ActionsStorage.builder()
                    .actions(Actions.builder()
                            .prevActions(Collections.EMPTY_LIST).build()).build();
        } catch (NullPointerException | ClassCastException e) {
            log.info("В session_state не найдены предыдущие действия");
            session_state = ActionsStorage.builder()
                    .actions(Actions.builder()
                            .prevActions(Collections.EMPTY_LIST).build()).build();
        }
    }
}
