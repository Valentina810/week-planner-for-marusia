package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.Action;
import com.github.valentina810.weekplannerformarusia.action.TypeAction;


public interface BaseHandlerResponsePhrase {
    String find(Action action);

    TypeAction getType();
}