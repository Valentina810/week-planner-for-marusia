package com.github.valentina810.weekplannerformarusia.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Request {
    private String original_utterance;
    private String command;
}