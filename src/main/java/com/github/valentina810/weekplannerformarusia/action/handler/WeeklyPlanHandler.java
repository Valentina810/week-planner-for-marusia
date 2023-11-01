package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.Action;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.context.Day;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

public class WeeklyPlanHandler implements BaseHandlerResponsePhrase {

    @Override
    public String find(Action action) {
        List<Day> collect;
        try {
            collect = action.getPersistentStorage().getWeekStorage()
                    .getWeek().getDays().stream()
                    .filter(e -> !e.getEvents().isEmpty()).collect(Collectors.toList());
            if (collect.isEmpty()) {
                return "У вас пока нет событий на этой неделе";
            } else {
                return "Ваши события " + collect.stream()
                        .map(day -> day.getDate() + " " + day.getEvents().stream()
                                .map(event -> event.getTime() + " " + event.getName())
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(" "));
            }
        } catch (NullPointerException e) {
            return "У вас пока нет событий на этой неделе";
        }
    }

    @Override
    public TypeAction getType() {
        return WEEKLY_PLAN;
    }
}