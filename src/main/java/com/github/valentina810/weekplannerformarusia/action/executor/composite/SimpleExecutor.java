package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.SIMPLE;

/**
 * Все обработчки с isSimple = true создаются как обработчики этого класса
 */
@Component
@RequiredArgsConstructor
public class SimpleExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return SIMPLE;
    }

    /**
     * Обработка для "простого" обработчика, который:
     * - не меняет состояние sessionStorage
     * - не меняет состояние  persistentStorage
     * - не завершает сессию
     * - всегда возвращает стандартный ответ:  messagePositive
     */
    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        Command command = getCommand(exParam.getTypeAction());
        return ResponseParameters.builder()
                .isEndSession(false)
                .respPhrase(command.getMessagePositive())
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}