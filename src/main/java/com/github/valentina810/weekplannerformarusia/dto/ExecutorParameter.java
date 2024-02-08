package com.github.valentina810.weekplannerformarusia.dto;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ExecutorParameter {
    private TypeAction typeAction;
    private String phrase;
    private SessionStorage sessionStorage;
    private PersistentStorage persistentStorage;
}