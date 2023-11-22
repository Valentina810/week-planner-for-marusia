package com.github.valentina810.weekplannerformarusia.action.parameterized.dayplan;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParameterForEventsForDateTest {
    private String testName;
    private String jsonFileSource;
    private String phrase;
    private String date;
    private String todayEvents;
    private String expectedResult;
}