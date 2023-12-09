package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;

@Getter
@Component
public class ExitHandler implements BaseHandler {

    private final ParametersHandler parametersHandler = new ParametersHandler();

    @Override
    public TypeAction getType() {
        return EXIT;
    }

    @Override
    public void getAction(UserRequest userRequest) {
        setDefaultValueParameters(parametersHandler);
        parametersHandler.setIsEndSession(true);
        parametersHandler.setRespPhrase(parametersHandler.getLoadCommand().getMessagePositive());
    }
}
