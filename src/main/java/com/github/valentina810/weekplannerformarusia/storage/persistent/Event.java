package com.github.valentina810.weekplannerformarusia.storage.persistent;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Event {
    private String time;
    private String name;
}