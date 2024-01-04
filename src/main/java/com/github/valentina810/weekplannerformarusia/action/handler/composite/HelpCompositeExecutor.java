package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;

@Component
@RequiredArgsConstructor
public class HelpCompositeExecutor implements BaseCompositeExecutor {
    @Override
    public TypeAction getType() {
        return HELP;
    }

    @Override
    public UnaryOperator<ParametersHandler> getActionExecute() {
        return parHandler ->
        {
            parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive());
            parHandler.getSessionStorage().addAction(
                    PrevAction.builder()
                            .number(0)
                            .operation(parHandler.getLoadCommand().getOperation())
                            .valueAction("").build());
            return parHandler;
        };
    }
}