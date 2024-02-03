package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Component
@RequiredArgsConstructor
public class WeeklyPlanExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return WEEKLY_PLAN;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        Map<String, List<Event>> days;
        String respPhrase;
        Command command = getCommand(exParam.getTypeAction());
        try {

            days = exParam.getPersistentStorage().getEventsByWeek();
            if (days.isEmpty()) {
                respPhrase = command.getMessageNegative();
            } else {
                respPhrase = days.keySet().stream().map(e ->
                                e + days.get(e).stream()
                                        .map(a -> a.getName() + " " + a.getTime())
                                        .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(" "));
            }
        } catch (NullPointerException e) {
            respPhrase = command.getMessageNegative();
        }
        return ResponseParameters.builder()
                .isEndSession(command.getIsEndSession())
                .respPhrase(respPhrase)
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}