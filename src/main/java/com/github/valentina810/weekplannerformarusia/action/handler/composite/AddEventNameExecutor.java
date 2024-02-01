package com.github.valentina810.weekplannerformarusia.action.handler.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.PrevAction;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_NAME;

@Component
@RequiredArgsConstructor
public class AddEventNameExecutor implements BaseExecutor {
    @Override
    public TypeAction getType() {
        return ADD_NAME;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        SessionStorage sessionStorage = exParam.getSessionStorage();
        Optional<PrevAction> lastPrevAction = sessionStorage.getActions().getLastPrevAction();
        sessionStorage.addPrevAction(PrevAction.builder()
                .prevOperation(lastPrevAction.isEmpty() ? null : lastPrevAction.get().getOperation())
                .operation(getType())
                .valueAction(exParam.getPhrase()).build());
        Command command = getCommand(exParam.getTypeAction());
        PersistentStorage persistentStorage = exParam.getPersistentStorage();
        if (command.getIsTerminal()) {//команда должна привести к выполнению действия
            persistentStorage.addEvent(Event.builder().build(), "среда");
        }
        return ResponseParameters.builder()
                .isEndSession(command.getIsEndSession())
                .respPhrase(command.getMessagePositive())
                .sessionStorage(sessionStorage)
                .persistentStorage(persistentStorage)
                .build();
    }
}
