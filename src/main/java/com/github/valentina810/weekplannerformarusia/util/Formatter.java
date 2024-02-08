package com.github.valentina810.weekplannerformarusia.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@UtilityClass
public class Formatter {
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public Function<LocalDate, String> convertDateToString = d -> d.format(DATE_FORMAT);
}