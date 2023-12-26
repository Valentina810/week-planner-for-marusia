package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT_MAIN_MENU;

@Component
@RequiredArgsConstructor
public class ExitMainMenuCompositeExecutor implements BaseCompositeExecutor {
    @Override
    public TypeAction getType() {
        return EXIT_MAIN_MENU;
    }

    @Override
    public UnaryOperator<ParametersHandler> getActionExecute() {
        return parHandler ->
        {
            parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive());
            parHandler.getSessionStorage().clear();
            return parHandler;
        };
    }
}