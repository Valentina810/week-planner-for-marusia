package com.github.valentina810.weekplannerformarusia.storage.persistent;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class Day {
    private String date;
    private List<Event> events;
}