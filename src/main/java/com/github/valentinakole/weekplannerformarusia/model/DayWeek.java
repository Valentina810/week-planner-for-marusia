package com.github.valentinakole.weekplannerformarusia.model;

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