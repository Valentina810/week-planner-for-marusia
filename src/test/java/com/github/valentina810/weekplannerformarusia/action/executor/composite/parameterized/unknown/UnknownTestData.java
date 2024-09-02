package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class UnknownTestData {

    static Stream<Arguments> providerUnknownTest() {

        return Stream.of(
                of(ParameterForUnknownTest.builder()
                        .phrase("PhraseUnknown")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("сегодня")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("завтра")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("план")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("понедельник")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("вторник")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("среда")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("четверг")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("пятница")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("суббота")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("воскресенье")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("часов")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("минут")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("часов минут")
                        .build()),
                of(ParameterForUnknownTest.builder()
                        .phrase("понедельник вторник")
                        .build())
        );
    }
}