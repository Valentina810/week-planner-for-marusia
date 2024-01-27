package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
        return ResponseParameters.builder()
                .isEndSession(getCommand().getIsEndSession())
                .respPhrase(getEventsForDate(LocalDate.now().plusDays(1), exParam.getPersistentStorage()))
                .sessionStorage(exParam.getSessionStorage())
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}