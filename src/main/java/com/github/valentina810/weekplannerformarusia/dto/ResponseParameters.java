package com.github.valentina810.weekplannerformarusia.dto;

import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ResponseParameters {
    @Setter
    private String respPhrase;
    private Boolean isEndSession;
    @Setter
    private SessionStorage sessionStorage;
    private PersistentStorage persistentStorage;
}