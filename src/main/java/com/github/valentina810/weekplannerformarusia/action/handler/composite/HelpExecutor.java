package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;

@Component
@RequiredArgsConstructor
public class HelpExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return HELP;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        return getResponseParametersForChainCommand(exParam);
    }
}