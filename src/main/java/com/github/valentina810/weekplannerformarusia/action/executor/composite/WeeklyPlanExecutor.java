package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import com.github.valentina810.weekplannerformarusia.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

import static java.util.Comparator.comparing;

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
        return Optional.ofNullable(exParam.getPersistentStorage()
                        .getEventsByWeek())
                .filter(events -> !events.isEmpty())
                .map(events -> getPhraseWithNotEmptyEvents(command, events))
                .orElse(command.getMessageNegative());
    }

    private String getPhraseWithNotEmptyEvents(Command command, Map<String, List<Event>> days) {
        TreeMap<LocalDate, String> sortedDays = new TreeMap<>(
                days.entrySet().stream()
                        .collect(Collectors.toMap(
                                dayDate -> Formatter.convertStringToDate.apply(dayDate.getKey()),
                                dayDate -> dayDate.getValue().stream()
                                        .sorted(comparing(Event::getTime))
                                        .map(event -> " " + event.getTime()+" " + event.getName())
                                        .collect(Collectors.joining(","))
                        ))
        );

        String eventsString = sortedDays.entrySet().stream()
                .map(dayDate -> Formatter.convertDateToString.apply(dayDate.getKey()) + dayDate.getValue())
                .collect(Collectors.joining(", "));

        return command.getMessagePositive() + eventsString;
    }
}