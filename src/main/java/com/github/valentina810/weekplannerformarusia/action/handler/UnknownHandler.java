package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;


public class UnknownHandler implements BaseHandler {

    @Override
    public String find(UserRequest userRequest) {
        return null;
    }

    @Override
    public TypeAction getType() {
        return null;
    }
}
