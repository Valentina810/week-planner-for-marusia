package com.github.valentina810.weekplannerformarusia.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Response {
    private String text;
    private String tts;
    private boolean end_session;
}