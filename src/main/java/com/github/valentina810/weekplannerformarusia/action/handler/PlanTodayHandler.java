package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.Action;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.context.Day;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;

public class PlanTodayHandler implements BaseHandlerResponsePhrase {

    @Override
    public String find(Action action) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<Day> collect;
        try {
            collect = action.getPersistentStorage().getWeekStorage().getWeek().getDays().stream()
                    .filter(e -> e.getDate().equals(today))
                    .filter(a -> !a.getEvents().isEmpty())
                    .collect(Collectors.toList());
            if (collect.isEmpty()) {
                return "У вас пока нет событий на сегодня";
            } else {
                return "Ваши события " + collect.stream()
                        .map(day -> day.getDate() + " " + day.getEvents().stream()
                                .map(event -> event.getTime() + " " + event.getName())
                                .collect(Collectors.joining(" ")))
                        .collect(Collectors.joining(" "));
            }
        } catch (NullPointerException e) {
            return "У вас пока нет событий на сегодня";
        }
    }

    @Override
    public TypeAction getType() {
        return TODAY_PLAN;
    }
}