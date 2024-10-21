package com.github.valentina810.weekplannerformarusia.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import static java.time.LocalDate.parse;

@UtilityClass
@Slf4j
public class Formatter {
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public Function<LocalDate, String> convertDateToString = d -> d.format(DATE_FORMAT);
    public Function<String, LocalDate> convertStringToDate = d -> parse(d, DATE_FORMAT);

    /**
     * @param tz - таймзона пользователя в формате "Europe/Moscow"
     * @return - текущая дата пользователя
     */
    public Function<String, LocalDate> getCurrentDateForTimeZone = tz ->
    {
        try {
            return ZonedDateTime.now(ZoneId.of(tz)).toLocalDate();
        } catch (DateTimeException e) {
            log.info("Не удалось получить текущее время пользователя для таймзоны {}", tz);
            return LocalDate.now();
        }
    };
}