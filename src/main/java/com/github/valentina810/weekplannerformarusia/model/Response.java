package com.github.valentina810.weekplannerformarusia.model;

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