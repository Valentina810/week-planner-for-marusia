package com.github.valentina810.weekplannerformarusia.storage.session;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.TreeSet;

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

    private Object session_state;
    @Getter
    private Actions actions;

    public Object getSession_state() {
        return !actions.getActions().isEmpty() ? actions : session_state;
    }

    public void getPrevActions(Object object) {
        try {
            JsonElement jsonElement = new Gson()
                    .fromJson(new Gson().toJson(object), JsonElement.class);
            actions = new Gson()
                    .fromJson(new Gson().toJson(jsonElement.getAsJsonObject()
                            .getAsJsonObject("actions")), Actions.class);
        } catch (NullPointerException e) {
            log.info("В session_state не найдены предыдущие действия");
        } finally {
            actions = Actions.builder().actions(new TreeSet<>()).build();
        }
    }
}
