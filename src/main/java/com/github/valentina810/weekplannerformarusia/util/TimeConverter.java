package com.github.valentina810.weekplannerformarusia.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Предоставляет возможность конвертации строки, которая представляет собой прописную запись времени вида
 * "двадцать три часа тридцать восемь минут" в строку вида 23:38
 */
@UtilityClass
public class TimeConverter {

    private static final Map<String, Integer> numbers = new HashMap<>();

    static {
        numbers.put("ноль", 0);
        numbers.put("один", 1);
        numbers.put("два", 2);
        numbers.put("три", 3);
        numbers.put("четыре", 4);
        numbers.put("пять", 5);
        numbers.put("шесть", 6);
        numbers.put("семь", 7);
        numbers.put("восемь", 8);
        numbers.put("девять", 9);
        numbers.put("десять", 10);
        numbers.put("одиннадцать", 11);
        numbers.put("двенадцать", 12);
        numbers.put("тринадцать", 13);
        numbers.put("четырнадцать", 14);
        numbers.put("пятнадцать", 15);
        numbers.put("шестнадцать", 16);
        numbers.put("семнадцать", 17);
        numbers.put("восемнадцать", 18);
        numbers.put("девятнадцать", 19);
        numbers.put("двадцать", 20);
        numbers.put("тридцать", 30);
        numbers.put("сорок", 40);
        numbers.put("пятьдесят", 50);
    }

    /**
     * Конвертирует строку на входе в число
     *
     * @param str строка в виде "двадцать три"
     * @return число в виде 23
     */
    public int convert(String str) {
        return Arrays.stream(str.split(" "))
                .mapToInt(word -> numbers.getOrDefault(word, 0))
                .sum();
    }

    /**
     * Возвращает из время из строки
     *
     * @param time - время в формате "двадцать три часа тридцать восемь минут"
     * @return - время в формате 23:38
     */
    public String getTime(String time) {
        String[] times = time.toLowerCase().split("час");
        int hours = times.length >= 1 ? convert(times[0]) : 0;
        int minutes = times.length >= 2 ? convert(times[1]) : 0;
        hours = hours > 23 ? 0 : hours;
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
    }
}