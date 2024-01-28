package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
        List<Day> days;
        String respPhrase;
        Command command = getCommand(exParam.getTypeAction());
        try {
            days = Optional.ofNullable(exParam.getPersistentStorage()
                            .getEventsByDay())
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
            if (days.isEmpty()) {
                respPhrase = command.getMessageNegative();
            } else {
                respPhrase = command.getMessagePositive() + days.stream()
                        .map(day -> day.getDate() + " " + day.getEvents().stream()
                                .map(event -> event.getTime() + " " + event.getName())
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