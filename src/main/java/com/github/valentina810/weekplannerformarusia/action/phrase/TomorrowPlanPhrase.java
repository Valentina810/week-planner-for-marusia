package com.github.valentina810.weekplannerformarusia.action.phrase;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;

@Component
@RequiredArgsConstructor
public class TomorrowPlanPhrase implements BasePhrase {
    @Override
    public String getPhrase() {
        return "план на завтра";
    }

    @Override
    public TypeAction getAction() {
        return TOMORROW_PLAN;
    }
}