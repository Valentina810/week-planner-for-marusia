package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;

@Component
@RequiredArgsConstructor
public class ExitCompositeExecutor implements BaseCompositeExecutor {
    @Override
    public TypeAction getType() {
        return EXIT;
    }

    @Override
    public UnaryOperator<ParametersHandler> getActionExecute() {
        return parHandler ->
        {
            parHandler.setIsEndSession(true);
            parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive());
            return parHandler;
        };
    }
}