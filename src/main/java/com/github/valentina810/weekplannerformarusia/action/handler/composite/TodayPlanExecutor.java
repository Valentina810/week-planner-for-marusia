package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
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
        return ResponseParameters.builder()
                .isEndSession(getCommand().getIsEndSession())
                .respPhrase(getEventsForDate(LocalDate.now(), exParam.getPersistentStorage()))
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}
