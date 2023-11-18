package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;

@Component
@RequiredArgsConstructor
public class PlanForTomorrowHandler implements BaseHandler, ReceiverEventsForDate {

    @Override
    public String find(UserRequest userRequest) {
        return getEventsForDate(userRequest.getState().getUser(),
                LocalDate.now().plusDays(1), "завтра");
    }

    @Override
    public TypeAction getType() {
        return TOMORROW_PLAN;
    }
}
