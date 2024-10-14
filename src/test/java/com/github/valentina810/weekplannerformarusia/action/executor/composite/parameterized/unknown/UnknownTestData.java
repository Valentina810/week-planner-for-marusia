package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.unknown.ParameterForUnknownTest.*;
import static org.junit.jupiter.params.provider.Arguments.of;

public class UnknownTestData {

    static Stream<Arguments> providerUnknownTest() {

        return Stream.of(
                of(builder()
                        .phrase("PhraseUnknown")
                        .build()),
                of(builder()
                        .phrase("")
                        .build()),
                of(builder()
                        .phrase("сегодня")
                        .build()),
                of(builder()
                        .phrase("завтра")
                        .build()),
                of(builder()
                        .phrase("план")
                        .build()),
                of(builder()
                        .phrase("понедельник")
                        .build()),
                of(builder()
                        .phrase("вторник")
                        .build()),
                of(builder()
                        .phrase("среда")
                        .build()),
                of(builder()
                        .phrase("четверг")
                        .build()),
                of(builder()
                        .phrase("пятница")
                        .build()),
                of(builder()
                        .phrase("суббота")
                        .build()),
                of(builder()
                        .phrase("воскресенье")
                        .build()),
                of(builder()
                        .phrase("часов")
                        .build()),
                of(builder()
                        .phrase("минут")
                        .build()),
                of(builder()
                        .phrase("часов минут")
                        .build()),
                of(builder()
                        .phrase("понедельник вторник")
                        .build())
        );
    }
}