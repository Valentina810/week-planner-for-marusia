package com.github.valentina810.weekplannerformarusia.context;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class Week {
    @Getter
    private List<Day> days;
}