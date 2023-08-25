package com.github.valentinakole.weekplannerformarusia.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Session {
    private String user_id;
    private String session_id;
    private int message_id;
}