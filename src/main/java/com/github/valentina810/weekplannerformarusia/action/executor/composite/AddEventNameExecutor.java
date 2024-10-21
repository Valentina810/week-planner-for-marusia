package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_DAY;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_NAME;
import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_TIME;

@Component
@RequiredArgsConstructor
public class AddEventNameExecutor implements BaseExecutor, TerminalExecutor {
    /**
     * Меняет persistentStorage на основе контекста из sessionStorage
     * и возвращает информацию для ответа на основе проведенных операций
     */
    Function<ExecutorParameter, String> getMessageInfo = (parameter) ->
    {
        Function<TypeAction, String> getValueAction = a ->
                parameter.getSessionStorage()
                        .getActions()
                        .getPrevActions()
                        .stream()
                        .filter(e -> a.equals(e.getOperation()))
                        .findFirst()
                        .get()
                        .getValueAction();
        return parameter.getPersistentStorage().addEvent(getValueAction.apply(ADD_DAY),
                getValueAction.apply(ADD_TIME),
                getValueAction.apply(ADD_NAME), parameter.getZoneId());
    };

    @Override
    public TypeAction getType() {
        return ADD_NAME;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam) {
        return getResponseParameters(exParam, getMessageInfo);
    }

    public ResponseParameters getResponseParameters(ExecutorParameter exParam,
                                                    Function<ExecutorParameter, String> getMessageInfo) {
        ResponseParameters responseParameters = getResponseParametersForIntermediateCommand(exParam);
        responseParameters.setRespPhrase(responseParameters.getRespPhrase()
                .replace("{messageInfo}",
                        getMessageInfo.apply(exParam)));
        responseParameters.setSessionStorage(null);
        return responseParameters;
    }

    @Override
    public ResponseParameters getResponseParameters(ExecutorParameter exParam, BiFunction<SessionStorage, PersistentStorage, String> getMessageInfo) {
        return getResponseParameters(exParam, this.getMessageInfo);
    }
}