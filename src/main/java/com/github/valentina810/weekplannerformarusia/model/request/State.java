package com.github.valentina810.weekplannerformarusia.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class State {
    /*session_state*/
    private Object session;

    /*user_state_update*/
    private Object user;
}