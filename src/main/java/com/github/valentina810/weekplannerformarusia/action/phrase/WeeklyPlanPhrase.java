package com.github.valentina810.weekplannerformarusia.action.phrase;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.WEEKLY_PLAN;

@Component
@RequiredArgsConstructor
public class WeeklyPlanPhrase implements BasePhrase {
    @Override
    public String getPhrase() {
        return "план на неделю";
    }

    @Override
    public TypeAction getAction() {
        return WEEKLY_PLAN;
    }
}