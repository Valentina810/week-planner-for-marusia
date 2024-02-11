package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        Command command = getCommand(exParam.getTypeAction());
        return ResponseParameters.builder()
                .isEndSession(command.getIsEndSession())
                .respPhrase(getResponsePhrase(exParam, command))
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }

    private String getResponsePhrase(ExecutorParameter exParam, Command command) {
        return Optional.ofNullable(exParam.getPersistentStorage().getEventsByWeek()).filter(events -> !events.isEmpty()).map(events -> getPhraseWithNotEmptyEvents(command, events)).orElse(command.getMessageNegative());
    }

    private String getPhraseWithNotEmptyEvents(Command command, Map<String, List<Event>> days) {
        String respPhrase;
        respPhrase = command.getMessagePositive() + days.keySet().stream().map(e ->
                        e + days.get(e).stream()
                                .map(a -> " " + a.getName() + " " + a.getTime())
                                .collect(Collectors.joining(",")))
                .collect(Collectors.joining(", "));
        return respPhrase;
    }
}