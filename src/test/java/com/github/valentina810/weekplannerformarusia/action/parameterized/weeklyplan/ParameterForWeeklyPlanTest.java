package com.github.valentina810.weekplannerformarusia.action.parameterized.weeklyplan;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParameterForWeeklyPlanTest {
    private String testName;
    private String jsonFileSource;
    private String expectedResult;
}