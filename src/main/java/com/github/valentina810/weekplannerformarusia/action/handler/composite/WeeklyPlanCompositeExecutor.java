package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Component
@RequiredArgsConstructor
public class WeeklyPlanCompositeExecutor implements BaseCompositeExecutor {
    @Override
    public TypeAction getType() {
        return WEEKLY_PLAN;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter executorParameter) {
        return null;
    }

//    @Override
//    public UnaryOperator<ResponseParameters> getActionExecute() {
//        return parHandler ->
//        {
//            List<Day> days;
//            try {
//                days = Optional.ofNullable(parHandler.getPersistentStorage()
//                                .getEventsByDay())
//                        .orElse(Collections.emptyList())
//                        .stream()
//                        .filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
//                if (days.isEmpty()) {
//                    parHandler.setRespPhrase(parHandler.getCommand().getMessageNegative());
//                } else {
//                    parHandler.setRespPhrase(parHandler.getCommand().getMessagePositive() + days.stream()
//                            .map(day -> day.getDate() + " " + day.getEvents().stream()
//                                    .map(event -> event.getTime() + " " + event.getName())
//                                    .collect(Collectors.joining(" ")))
//                            .collect(Collectors.joining(" ")));
//                }
//            } catch (NullPointerException e) {
//                parHandler.setRespPhrase(parHandler.getCommand().getMessageNegative());
//            }
//            return parHandler;
//        };
//    }
}