package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_DAY;

@Component
@RequiredArgsConstructor
public class AddDayExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return ADD_DAY;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        return getResponseParametersForIntermediateCommand(exParam);
    }
}