package com.github.valentina810.weekplannerformarusia.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Session {
    private String user_id;
    private String session_id;
    private int message_id;
}