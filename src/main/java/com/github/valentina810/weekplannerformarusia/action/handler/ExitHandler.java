package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;

@Component
@RequiredArgsConstructor
public class ExitHandler implements BaseHandler {

    @Override
    public String find(UserRequest userRequest) {
        return "До свидания!";
    }

    @Override
    public TypeAction getType() {
        return EXIT;
    }
}
