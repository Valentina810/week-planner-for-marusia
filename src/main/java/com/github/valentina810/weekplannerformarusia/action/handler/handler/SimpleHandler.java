package com.github.valentina810.weekplannerformarusia.action.handler.handler;

import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Getter;

/**
 * Простой обработчик, который:
 * - не модифицирует данные в session_state и user_state_update
 * - не содержит вложенных команд
 * - в качестве ответа использует простую фразу (без действий по вычислению её параметров)
 */
public class SimpleHandler {
    @Getter
    protected ParametersHandler parametersHandler;

    public SimpleHandler(ParametersHandler parametersHandler) {
        this.parametersHandler = parametersHandler;
    }

    public void execute() {
        setDefaultValueParameters(parametersHandler);
        parametersHandler.setRespPhrase(parametersHandler.getLoadCommand().getMessagePositive());
    }

    /**
     * Установить значения по умолчанию для параметров
     * (случай, когда сессия не завершается и не модифицируются данные в
     * хранилищах)
     */
    void setDefaultValueParameters(ParametersHandler parameters) {
        parameters.setIsEndSession(false);

        Object session = parameters.getUserRequest().getState().getSession();
        SessionStorage sessionStorage = new SessionStorage();
        sessionStorage.setSession_state(session);
        parameters.setSessionStorage(sessionStorage);

        Object user = parameters.getUserRequest().getState().getUser();
        PersistentStorage persistentStorage = new PersistentStorage();
        persistentStorage.getWeekEvents(user);
        parameters.setPersistentStorage(persistentStorage);
    }
}