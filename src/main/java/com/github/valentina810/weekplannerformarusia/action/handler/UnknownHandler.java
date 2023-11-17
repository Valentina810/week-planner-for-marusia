package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.NONE;

@Component
@RequiredArgsConstructor
public class UnknownHandler implements BaseHandler {

    @Override
    public String find(UserRequest userRequest) {
        return "Получена неизвестная команда";
    }

    @Override
    public TypeAction getType() {
        return NONE;
    }
}
