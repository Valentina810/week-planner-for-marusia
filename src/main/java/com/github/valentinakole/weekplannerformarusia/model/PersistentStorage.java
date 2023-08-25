package com.github.valentinakole.weekplannerformarusia.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PersistentStorage {
    private DayWeek[] data;
}