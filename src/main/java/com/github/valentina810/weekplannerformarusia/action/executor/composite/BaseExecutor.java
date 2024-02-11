package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.CommandLoader;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.github.valentina810.weekplannerformarusia.util.Formatter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BaseExecutor {

    TypeAction getType();

    ResponseParameters getResponseParameters(ExecutorParameter executorParameter);

    default Command getCommand(TypeAction typeAction) {
        return CommandLoader.get(typeAction);
    }

    /**
     * Выбирает из массива дней все события на определённую дату
     */
    default String getEventsForDate(Command command, LocalDate date, PersistentStorage persistentStorage) {
        String defaultMessage = command.getMessageNegative();
        if (defaultMessage != null) {
            return getMessage(command.getMessagePositive(),
                    persistentStorage.getEventsByDay(Formatter.convertDateToString.apply(date)),
                    defaultMessage);
        } else {
            return "";
        }
    }

    /**
     * Формирует список событий с разделителями
     */
    private String getMessage(String messagePositive, List<Event> eventsByDay, String defaultMessage) {
        return eventsByDay.isEmpty() ? defaultMessage :
                messagePositive + eventsByDay.stream()
                        .map(event -> event.getTime() + " " + event.getName())
                        .collect(Collectors.joining(", "));
    }

    /**
     * Обработка команды из цепочки + к стандартной обработке дозаписать предыдыщее событие в sessionStorage
     */
    default ResponseParameters getResponseParametersForChainCommand(ExecutorParameter exParam) {
        SessionStorage sessionStorage = exParam.getSessionStorage();
        Optional<PrevAction> lastPrevAction = sessionStorage.getActions().getLastPrevAction();
        sessionStorage.addPrevAction(PrevAction.builder()
                .prevOperation(lastPrevAction.map(PrevAction::getOperation).orElse(null))
                .operation(getType())
                .valueAction(exParam.getPhrase()).build());
        Command command = getCommand(exParam.getTypeAction());
        return ResponseParameters.builder()
                .isEndSession(command.getIsEndSession())
                .respPhrase(command.getMessagePositive())
                .sessionStorage(sessionStorage)
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}