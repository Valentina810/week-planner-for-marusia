package com.github.valentina810.weekplannerformarusia.storage.session;

import lombok.Builder;

@Builder
public class ActionsStorage {
    private Actions actions;

    public Actions getActions() {
        return actions == null ? Actions.builder().build() : actions;
    }
}