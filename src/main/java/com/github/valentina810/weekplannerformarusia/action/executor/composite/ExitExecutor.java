package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;

@Component
@RequiredArgsConstructor
public class ExitExecutor implements BaseExecutor {

    @Override
    public TypeAction getType() {
        return EXIT;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        return ResponseParameters.builder()
                .isEndSession(true)
                .respPhrase(getCommand(exParam.getTypeAction()).getMessagePositive())
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}