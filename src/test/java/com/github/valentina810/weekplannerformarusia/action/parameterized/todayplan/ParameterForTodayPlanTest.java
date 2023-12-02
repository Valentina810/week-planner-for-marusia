package com.github.valentina810.weekplannerformarusia.action.parameterized.todayplan;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParameterForTodayPlanTest {
    private String testName;
    private String jsonFileSource;
    private String todayDate;
    private String todayEvents;
    private String expectedResult;
}