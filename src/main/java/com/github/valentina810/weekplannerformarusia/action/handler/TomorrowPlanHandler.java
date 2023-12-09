package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;


@Getter
@Component
public class TomorrowPlanHandler implements BaseHandler {

    private final ParametersHandler parametersHandler = new ParametersHandler();

    public TypeAction getType() {
        return TOMORROW_PLAN;
    }

    public void getAction(UserRequest userRequest) {
        setDefaultValueParameters(parametersHandler);
        parametersHandler.setRespPhrase(getEventsForDate(LocalDate.now().plusDays(1), parametersHandler));
    }
}
