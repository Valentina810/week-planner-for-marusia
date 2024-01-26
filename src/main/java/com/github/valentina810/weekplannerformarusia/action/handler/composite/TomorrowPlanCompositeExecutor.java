package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;

@Component
@RequiredArgsConstructor
public class TomorrowPlanCompositeExecutor implements BaseCompositeExecutor {
    @Override
    public TypeAction getType() {
        return TOMORROW_PLAN;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter executorParameter) {
        return null;
    }

    @Override
    public Command getCommand() {
        return BaseCompositeExecutor.super.getCommand();
    }

//    @Override
//    public UnaryOperator<ResponseParameters> getActionExecute() {
//        return parHandler ->
//        {
//            parHandler.setRespPhrase(getEventsForDate(LocalDate.now().plusDays(1), parHandler));
//            return parHandler;
//        };
//    }
}