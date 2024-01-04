package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.LoadCommand;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParametersHandler {

    private LoadCommand loadCommand;
    private UserRequest userRequest;
    private SessionStorage sessionStorage;
    private PersistentStorage persistentStorage;
    private String respPhrase;
    private Boolean isEndSession;
}