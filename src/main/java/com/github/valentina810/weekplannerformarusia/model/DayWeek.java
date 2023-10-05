package com.github.valentina810.weekplannerformarusia.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DayWeek {
    private String date;
    private String timeTable;
}