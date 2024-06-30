package com.github.valentina810.weekplannerformarusia.storage.persistent;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class PersistentStorageData {
    static Stream<Arguments> providerPersistentStorageTest() {

        return Stream.of(
                of("понедельник"),
                of("вторник"),
                of("среда"),
                of("четверг"),
                of("пятница"),
                of("суббота"),
                of("воскресенье"),
                of("понедельник вторник среда"),
                of("пятница вторник"), //тут должны получить пятницу
                of("сегодня вторник"),
                of("модет четверг а может и нет"),
                of("пятниц"),
                of("енье"),
                of("лала"),
                of("ноль"),
                of("..."),
                of("")
        );
    }
}