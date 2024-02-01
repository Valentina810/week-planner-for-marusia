package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_TIME;

@Component
@RequiredArgsConstructor
public class AddTimeExecutor implements BaseExecutor{
    @Override
    public TypeAction getType() {
        return  ADD_TIME;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        SessionStorage sessionStorage = exParam.getSessionStorage();
        sessionStorage.addPrevAction(PrevAction.builder()
                .operation(getType())
                .valueAction(exParam.getPhrase()).build());
        Command command = getCommand(exParam.getTypeAction());
        return ResponseParameters.builder()
                .isEndSession(command.getIsEndSession())
                .respPhrase(command.getMessagePositive())
                .sessionStorage(sessionStorage)
                .persistentStorage(exParam.getPersistentStorage())
                .build();
    }
}