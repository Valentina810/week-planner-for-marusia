package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.TODAY_PLAN;

@Component
@RequiredArgsConstructor
public class TodayPlanExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return TODAY_PLAN;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        Command command = getCommand(exParam.getTypeAction());
        return ResponseParameters.builder()
                .isEndSession(command.getIsEndSession())
                .respPhrase(getEventsForDate(command, LocalDate.now(), exParam.getPersistentStorage()))
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}
