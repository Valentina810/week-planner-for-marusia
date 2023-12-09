package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;


@Getter
@Component
public class TodayPlanHandler implements BaseHandler {

    private final ParametersHandler parametersHandler = new ParametersHandler();

    public void getAction(UserRequest userRequest) {
        setDefaultValueParameters(parametersHandler);
        parametersHandler.setRespPhrase(getEventsForDate(LocalDate.now(), parametersHandler));
    }

    public TypeAction getType() {
        return TODAY_PLAN;
    }
}