package com.github.valentina810.weekplannerformarusia.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
/**
 * Событие
 */
public class Event {
    private String time;
    private String name;
}