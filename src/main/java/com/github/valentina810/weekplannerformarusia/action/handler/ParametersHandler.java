package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametersHandler {

    private LoadCommand loadCommand;
    private UserRequest userRequest;
    private SessionStorage sessionStorage = new SessionStorage();
    private PersistentStorage persistentStorage = new PersistentStorage();
    private String respPhrase;
    private Boolean isEndSession;
}