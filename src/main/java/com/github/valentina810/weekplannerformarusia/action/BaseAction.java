package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.context.Day;
import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaseAction {
    @Setter
    private SessionStorage sessionStorage;
    @Setter
    private PersistentStorage persistentStorage;
    private String message;
    @Setter
    private PrevAction prevAction;
    private Boolean isEndSession;

    public BaseAction replyWeeklyPlan() {
        this.persistentStorage.getWeekEvents();
        isEndSession = false;
        message = getWeekEventsData();
        return this;
    }

    /**
     * Формирует фразу с ответом на основе данных о событии в хранилище
     *
     * @return - ответная фраза
     */
    private String getWeekEventsData() {
        List<Day> collect;
        try {
            collect = persistentStorage.getWeekStorage().getWeek().getDays().stream().filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
            if (collect.isEmpty()) {
                return "У вас пока нет событий";
            } else {
                return "Ваши события " + collect.stream()
                        .map(day -> day.getDate() + " " + day.getEvents().stream()
                                .map(event -> event.getName() + " " + event.getTime())
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(" "));
            }
        } catch (NullPointerException e) {
            return "У вас пока нет событий";
        }
    }
}