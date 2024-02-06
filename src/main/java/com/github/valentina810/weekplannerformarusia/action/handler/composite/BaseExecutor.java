package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.CommandLoader;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            List<Event> eventsByDay = getDaysEvents(date, persistentStorage);
            return getMessage(command, eventsByDay, defaultMessage);
        } else {
            return "";
        }
    }

    private String getMessage(Command command, List<Event> eventsByDay, String defaultMessage) {
        return eventsByDay.isEmpty() ? defaultMessage :
                command.getMessagePositive() + eventsByDay.stream()
                        .map(event -> event.getName() + " " + event.getTime())
                        .collect(Collectors.joining(" "));
    }

    private static List<Event> getDaysEvents(LocalDate date, PersistentStorage persistentStorage) {
        return persistentStorage
                .getEventsByDay(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))); //#todo паттерн вынести в константу
    }

    /**
     * Обработка команды из цепочки + к стандартной обработке дозаписать предыдыщее событие в sessionStorage
     */
    default ResponseParameters getResponseParametersForChainCommand(ExecutorParameter exParam) {
        SessionStorage sessionStorage = exParam.getSessionStorage();
        Optional<PrevAction> lastPrevAction = sessionStorage.getActions().getLastPrevAction();
        sessionStorage.addPrevAction(PrevAction.builder()
                .prevOperation(lastPrevAction.isEmpty() ? null : lastPrevAction.get().getOperation())
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