package com.github.valentina810.weekplannerformarusia.storage.persistent;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Day {
    private String date;
    private List<Event> events;
}