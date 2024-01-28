package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.SIMPLE;

@Component
@RequiredArgsConstructor
public class SimpleExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return SIMPLE;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        TypeAction typeAction = exParam.getTypeAction();
        Command command = getCommand(typeAction);
        return ResponseParameters.builder()
                .isEndSession(false)
                .respPhrase(command.getMessagePositive())
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}