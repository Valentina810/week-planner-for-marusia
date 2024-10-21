package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TOMORROW_PLAN;

@Component
@RequiredArgsConstructor
public class TomorrowPlanExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return TOMORROW_PLAN;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        Command command = getCommand(exParam.getTypeAction());
        return ResponseParameters.builder()
                .isEndSession(command.getIsEndSession())
                .respPhrase(getEventsForDate(command, Formatter.getCurrentDateForTimeZone.apply(exParam.getZoneId()).plusDays(1), exParam.getPersistentStorage()))
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}