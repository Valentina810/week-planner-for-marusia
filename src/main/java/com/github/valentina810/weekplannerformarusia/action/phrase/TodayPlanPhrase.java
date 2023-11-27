package com.github.valentina810.weekplannerformarusia.action.phrase;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;

@Component
@RequiredArgsConstructor
public class TodayPlanPhrase implements BasePhrase {
    @Override
    public String getPhrase() {
        return "план на сегодня";
    }

    @Override
    public TypeAction getAction() {
        return TODAY_PLAN;
    }
}