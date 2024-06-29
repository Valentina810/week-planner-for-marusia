package com.github.valentina810.weekplannerformarusia.storage.persistent;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class PersistentStorageData {
    static Stream<Arguments> providerPersistentStorageTest() {

        return Stream.of(
                Arguments.of("понедельник"),
                Arguments.of("вторник"),
                Arguments.of("среда"),
                Arguments.of("четверг"),
                Arguments.of("пятница"),
                Arguments.of("суббота"),
                Arguments.of("воскресенье"),
                Arguments.of("понедельник вторник среда"),
                Arguments.of("пятница вторник"),
                Arguments.of("пятниц"),
                Arguments.of("енье"),
                Arguments.of("лала"),
                Arguments.of("ноль"),
                Arguments.of("..."),
                Arguments.of("")
        );
    }
}