package com.github.valentina810.weekplannerformarusia.action.parameterized.dayplan;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ParameterForEventsForDateTest {
    private String testName;
    private String jsonFileSource;
    private String phrase;
    private String date;
    private String todayEvents;
    private String expectedResult;
}