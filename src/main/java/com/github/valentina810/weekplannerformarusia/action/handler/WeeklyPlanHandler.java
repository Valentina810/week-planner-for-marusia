package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Getter
@Component
public class WeeklyPlanHandler implements BaseHandler {

    private final ParametersHandler parametersHandler = new ParametersHandler();

    public void getAction(UserRequest userRequest) {
        setDefaultValueParameters(parametersHandler);
        List<Day> collect;
        try {
            collect = parametersHandler.getPersistentStorage().getWeekStorage()
                    .getWeek().getDays().stream()
                    .filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
            if (collect.isEmpty()) {
                parametersHandler.setRespPhrase(parametersHandler.getLoadCommand().getMessageNegative());
            } else {
                parametersHandler.setRespPhrase(parametersHandler.getLoadCommand().getMessagePositive() + collect.stream()
                        .map(day -> day.getDate() + " " + day.getEvents().stream()
                                .map(event -> event.getTime() + " " + event.getName())
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(" ")));
            }
        } catch (NullPointerException e) {
            parametersHandler.setRespPhrase(parametersHandler.getLoadCommand().getMessageNegative());
        }
    }

    public TypeAction getType() {
        return WEEKLY_PLAN;
    }
}
