package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Day;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;

@Component
@RequiredArgsConstructor
public class TodayPlanHandler implements BaseHandler {

    @Override
    public String find(UserRequest userRequest) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<Day> collect;
        try {
            PersistentStorage persistentStorage = new PersistentStorage();
            persistentStorage.getWeekEvents(userRequest.getState().getUser());
            collect = persistentStorage.getWeekStorage().getWeek().getDays().stream()
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