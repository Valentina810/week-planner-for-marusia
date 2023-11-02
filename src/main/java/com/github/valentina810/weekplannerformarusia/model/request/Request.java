package com.github.valentina810.weekplannerformarusia.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Request {
    private String original_utterance;
    private String command;
}