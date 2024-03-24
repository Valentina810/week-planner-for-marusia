package com.github.valentina810.weekplannerformarusia.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeConverterTest {

    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.util.TimeConverterTestData#providerTimeTest")
    void getTime(String timeToString, String timeToTime) {
        assertEquals(timeToTime, TimeConverter.getTime(timeToString));
    }
}