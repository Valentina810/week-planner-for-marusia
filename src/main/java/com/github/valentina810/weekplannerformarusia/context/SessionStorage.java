package com.github.valentina810.weekplannerformarusia.context;


import com.github.valentina810.weekplannerformarusia.action.PrevAction;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Хранилище, которое хранит данные в контексте сессии
 * поле session_state
 * лимит размера json-объекта session_state — 5 Кбайт
 * состояние теряется в таких случаях:
 * - пользователь выходит из скилла;
 * - скилл сам явно завершает работу, передав "end_session": true;
 * - выход происходит по таймауту, когда пользователь не отвечает некоторое время (1 минуту).
 * https://dev.vk.com/ru/marusia/session-state
 */
@Builder
@Slf4j
public class SessionStorage {
    //содержит название предыдущей активности
    @Getter
    private Object session_state;

    /**
     * Возвращает код предыдущей активности, если она была
     *
     * @return PrevAction - код предыдущей активности и её значение
     */
    public PrevAction getPrevAction() {
        PrevAction action = PrevAction.builder()
                .typeAction(TypeAction.NONE)
                .valueAction("")
                .build();
        try {
            JSONObject prevAction = new JSONObject(session_state).getJSONObject("prevAction");
            action.setTypeAction(TypeAction.valueOf(prevAction.getString("typeAction")));
            action.setValueAction(prevAction.getString("valueAction"));
        } catch (JSONException | NullPointerException e) {
            log.info("В session_state не найдено предыдущее действие");
        }
        return action;
    }
}
