package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT_MAIN_MENU;

@Component
@RequiredArgsConstructor
public class ExitMainMenuExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return EXIT_MAIN_MENU;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter executorParameter) {
        return null;
    }

    @Override
    public Command getCommand() {
        return BaseExecutor.super.getCommand();
    }

//    @Override
//    public UnaryOperator<ResponseParameters> getActionExecute() {
//        return parHandler ->
//        {
//            parHandler.setRespPhrase(parHandler.getCommand().getMessagePositive());
//            parHandler.getSessionStorage().clear();
//            return parHandler;
//        };
//    }
}