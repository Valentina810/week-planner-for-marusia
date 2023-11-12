package com.github.valentina810.weekplannerformarusia.storage.session;


import com.github.valentina810.weekplannerformarusia.action.PrevAction;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

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

    public void getPrevAction() {
        PrevAction action = PrevAction.builder()
                .typeAction(TypeAction.NONE)
                .valueAction("")
                .build();
        try {
            JSONObject prevAction = new JSONObject(new Gson().toJson(session_state)).getJSONObject("prevAction");
            action.setTypeAction(TypeAction.valueOf(prevAction.getString("typeAction")));
            action.setValueAction(prevAction.getString("valueAction"));
            session_state = action;
        } catch (JSONException | NullPointerException e) {
            log.info("В session_state не найдено предыдущее действие");
        }
    }
}
