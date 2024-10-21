package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class UnknownTestData {

    static Stream<Arguments> providerUnknownTest() {
        return Stream.of(
                of("PhraseUnknown"),
                of(""),
                of("сегодня"),
                of("завтра"),
                of("план"),
                of("понедельник"),
                of("вторник"),
                of("среда"),
                of("четверг"),
                of("пятница"),
                of("суббота"),
                of("воскресенье"),
                of("часов"),
                of("минут"),
                of("часов минут"),
                of("понедельник вторник")
        );
    }
}