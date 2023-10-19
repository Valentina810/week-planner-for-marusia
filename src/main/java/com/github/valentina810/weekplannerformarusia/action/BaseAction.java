package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.context.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.context.SessionStorage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BaseAction {
    private SessionStorage sessionStorage;
    private PersistentStorage persistentStorage;
    private String message;

    public void action() {
    }
}