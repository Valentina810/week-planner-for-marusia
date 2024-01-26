package com.github.valentina810.weekplannerformarusia.dto;

import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseParameters {
    private String respPhrase;
    private Boolean isEndSession;
    private SessionStorage sessionStorage;
    private PersistentStorage persistentStorage;
}