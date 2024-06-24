package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.hello;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ParameterHelloTestDataTest {
    private String testName;
    private String phrase;
    private String jsonRequest;
    private String expectedPhrase;
    private boolean isEndSession;
}