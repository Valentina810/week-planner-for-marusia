package com.github.valentina810.weekplannerformarusia.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static java.util.Map.entry;


/**
 * Содержит метод для конвертации даты в "человеческий формат" для озвучивания, пример:
 * 'понедельник двадцать девятое марта'
 */
@UtilityClass
public class DateConverter {
    private final Map<String, String> mounts = Map.ofEntries(
            entry("01", "января"),
            entry("02", "февраля"),
            entry("03", "марта"),
            entry("04", "апреля"),
            entry("05", "мая"),
            entry("06", "июня"),
            entry("07", "июля"),
            entry("08", "агуста"),
            entry("09", "сентября"),
            entry("10", "октября"),
            entry("11", "ноября"),
            entry("12", "декабря"));

    private final Map<String, String> days = Map.ofEntries(
            entry("01", "первое"),
            entry("02", "второе"),
            entry("03", "третье"),
            entry("04", "четвертое"),
            entry("05", "пятое"),
            entry("06", "шестое"),
            entry("07", "седьмое"),
            entry("08", "восьмое"),
            entry("09", "девятое"),
            entry("10", "десятое"),
            entry("11", "одиннадцатое"),
            entry("12", "двенадцатое"),
            entry("13", "тринадцатое"),
            entry("14", "четырнадцатое"),
            entry("15", "пятнадцатое"),
            entry("16", "шестнадцатое"),
            entry("17", "семнадцатое"),
            entry("18", "восемнадцатое"),
            entry("19", "девятнадцатое"),
            entry("20", "двадцатое"),
            entry("21", "двадцать первое"),
            entry("22", "двадцать второе"),
            entry("23", "двадцать третье"),
            entry("24", "двадцать четвертое"),
            entry("25", "двадцать пятое"),
            entry("26", "двадцать шестое"),
            entry("27", "двадцать седьмое"),
            entry("28", "двадцать восьмое"),
            entry("29", "двадцать девятое"),
            entry("30", "тридцатое"),
            entry("31", "тридцать первое"));

    private final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("dd");
    private final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MM");

    public Function<LocalDate, String> convertDate = date ->
            new StringBuilder()
                    .append(getRusDayOfWeek(date))
                    .append(" ")
                    .append(days.get(date.format(DAY)))
                    .append(" ")
                    .append(mounts.get(date.format(MONTH))).toString();

    private String getRusDayOfWeek(LocalDate date) {
        String result = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
        return switch (result) {
            case "среда" -> "среду";
            case "пятница" -> "пятницу";
            case "суббота" -> "субботу";
            default -> result;
        };
    }
}