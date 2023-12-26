package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Component
@RequiredArgsConstructor
public class WeeklyPlanCompositeExecutor implements BaseCompositeExecutor {
    @Override
    public TypeAction getType() {
        return WEEKLY_PLAN;
    }

    @Override
    public UnaryOperator<ParametersHandler> getActionExecute() {
        return parHandler ->
        {
            List<Day> collect;
            try {
                collect = parHandler.getPersistentStorage().getWeekStorage()
                        .getWeek().getDays().stream()
                        .filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
                if (collect.isEmpty()) {
                    parHandler.setRespPhrase(parHandler.getLoadCommand().getMessageNegative());
                } else {
                    parHandler.setRespPhrase(parHandler.getLoadCommand().getMessagePositive() + collect.stream()
                            .map(day -> day.getDate() + " " + day.getEvents().stream()
                                    .map(event -> event.getTime() + " " + event.getName())
                                    .collect(Collectors.joining(" ")))
                            .collect(Collectors.joining(" ")));
                }
            } catch (NullPointerException e) {
                parHandler.setRespPhrase(parHandler.getLoadCommand().getMessageNegative());
            }
            return parHandler;
        };
    }
}