package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;

@Component
@RequiredArgsConstructor
public class HelpHandler implements BaseHandler {

    @Override
    public String find(UserRequest userRequest) {
        return "Выберите раздел: команды, как добавить событие, об авторе";
    }

    @Override
    public TypeAction getType() {
        return HELP;
    }
}
