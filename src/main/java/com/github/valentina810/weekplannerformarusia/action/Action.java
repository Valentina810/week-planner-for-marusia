package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.action.handler.BaseHandlerResponsePhrase;
import com.github.valentina810.weekplannerformarusia.action.handler.PlanTodayHandler;
import com.github.valentina810.weekplannerformarusia.action.handler.WeeklyPlanHandler;
import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_EVENT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.NONE;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    @Setter
    private SessionStorage sessionStorage;
    @Setter
    private PersistentStorage persistentStorage;
    private String message;
    @Setter
    private PrevAction prevAction;
    private Boolean isEndSession;
    private Map<TypeAction, BaseHandlerResponsePhrase> handlers;

    public Action reply() {
        fillHandlers();
        BaseHandlerResponsePhrase baseHandlerResponsePhrase = handlers.get(getAction(message));
        if (baseHandlerResponsePhrase != null) {
            message = baseHandlerResponsePhrase.find(this);
            persistentStorage.getWeekEvents();
            isEndSession = false;
        } else {
            throw new RuntimeException("Отсутствует реализация для " + getAction(message));
        }
        return this;
    }

    /**
     * Возвращает тип активности, который удалось выделить из фразы пользователя
     *
     * @param message - фраза
     * @return - выделенная активность
     */
    private TypeAction getAction(String message) {
        return switch (message) {
            case "план на неделю" -> WEEKLY_PLAN;
            case "план на сегодня" -> TODAY_PLAN;
            case "план на завтра" -> TOMORROW_PLAN;
            case "добавь событие" -> ADD_EVENT;
            case "справка" -> HELP;
            case "выход" -> EXIT;
            default -> NONE;
        };
    }

    /**
     * Заполнить мапу обработчиков
     */
    private void fillHandlers() {
        if (handlers == null || handlers.isEmpty()) {
            handlers = new HashMap<>();
            handlers.put(WEEKLY_PLAN, new WeeklyPlanHandler());
            handlers.put(TODAY_PLAN, new PlanTodayHandler());
        }
    }
}