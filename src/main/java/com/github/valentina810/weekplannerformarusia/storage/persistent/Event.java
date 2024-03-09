package com.github.valentina810.weekplannerformarusia.storage.persistent;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Event {
    private String time;
    private String name;
}