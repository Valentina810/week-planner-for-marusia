package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ParameterForUnknownTest {
    private String testName;
    private String jsonFileSource;
    private String testDate;
    private String testEvents;
    private String phrase;
    private String expectedResult;
}