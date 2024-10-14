package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.storage.persistent;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.storage.persistent.ParameterForPersistentStorageTest.*;
import static org.junit.jupiter.params.provider.Arguments.of;

public class PersistentStorageTestData {

    static Stream<Arguments> providerTimeZoneTest() {

        return Stream.of(
                of(builder()
                        .eventDay("понедельник")
                        .timeZone("Europe/Moscow")
                        .build()),
                of(builder()
                        .eventDay("вторник")
                        .timeZone("Europe/Moscow")
                        .build()),
                of(builder()
                        .eventDay("среда")
                        .timeZone("Europe/Moscow")
                        .build()),
                of(builder()
                        .eventDay("четверг")
                        .timeZone("Europe/Moscow")
                        .build()),
                of(builder()
                        .eventDay("пятница")
                        .timeZone("Europe/Moscow")
                        .build()),
                of(builder()
                        .eventDay("суббота")
                        .timeZone("Europe/Moscow")
                        .build()),
                of(builder()
                        .eventDay("воскресенье")
                        .timeZone("Europe/Moscow")
                        .build()),
                of(builder()
                        .eventDay("понедельник")
                        .timeZone("Asia/Yerevan")
                        .build()),
                of(builder()
                        .eventDay("вторник")
                        .timeZone("Asia/Yerevan")
                        .build()),
                of(builder()
                        .eventDay("среда")
                        .timeZone("Asia/Yerevan")
                        .build()),
                of(builder()
                        .eventDay("четверг")
                        .timeZone("Asia/Yerevan")
                        .build()),
                of(builder()
                        .eventDay("пятница")
                        .timeZone("Asia/Yerevan")
                        .build()),
                of(builder()
                        .eventDay("суббота")
                        .timeZone("Asia/Yerevan")
                        .build()),
                of(builder()
                        .eventDay("воскресенье")
                        .timeZone("Asia/Yerevan")
                        .build()),
                of(builder()
                        .eventDay("понедельник")
                        .timeZone("Australia/Sydney")
                        .build()),
                of(builder()
                        .eventDay("вторник")
                        .timeZone("Australia/Sydney")
                        .build()),
                of(builder()
                        .eventDay("среда")
                        .timeZone("Australia/Sydney")
                        .build()),
                of(builder()
                        .eventDay("четверг")
                        .timeZone("Australia/Sydney")
                        .build()),
                of(builder()
                        .eventDay("пятница")
                        .timeZone("Australia/Sydney")
                        .build()),
                of(builder()
                        .eventDay("суббота")
                        .timeZone("Australia/Sydney")
                        .build()),
                of(builder()
                        .eventDay("воскресенье")
                        .timeZone("Australia/Sydney")
                        .build())
        );
    }
}
