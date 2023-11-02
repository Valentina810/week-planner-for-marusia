package com.github.valentina810.weekplannerformarusia.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class State {
    /*session_state*/
    private Object session;

    /*user_state_update*/
    private Object user;
}