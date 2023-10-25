package com.github.valentina810.weekplannerformarusia.context;

import lombok.Builder;
import lombok.Getter;

@Builder
public class WeekStorage {
    @Getter
    private Week week;
}