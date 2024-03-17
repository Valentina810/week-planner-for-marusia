package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;

import java.util.function.BiFunction;

/**
 * Интерфейес для обработчика, который содержит метод модификации постоянного хранилища
 */
public interface TerminalExecutor {
    /**
     * Метод модифицирует persistentStorage
     *
     * @param exParam        - входные данные
     * @param getMessageInfo - функция, которая модифицирует persistentStorage и на выходе
     *                       возвращает сообщение, собранное на основе контекста и данных для обработки
     * @return - выходные данные, с уже модифицированным persistentStorage
     */
    ResponseParameters getResponseParameters(ExecutorParameter exParam,
                                             BiFunction<SessionStorage, PersistentStorage, String> getMessageInfo);
}