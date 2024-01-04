package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;

@Component
@RequiredArgsConstructor
public class TomorrowPlanCompositeExecutor implements BaseCompositeExecutor{
    @Override
    public TypeAction getType() {
        return TOMORROW_PLAN;
    }

    @Override
    public UnaryOperator<ParametersHandler> getActionExecute() {
        return parHandler ->
        {
            parHandler.setRespPhrase(getEventsForDate(LocalDate.now().plusDays(1), parHandler));
            return parHandler;
        };
    }
}