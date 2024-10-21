package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class HelpTestData {
    private static final String TEMPLATE_JSON = "action/plantodate/PlanEmpty.json";

    static Stream<Arguments> providerHelpTest() {

        return Stream.of(
                of(ParameterWithPrevActionsTest.builder()
                        .testName("getHelpMessage_whenCallCommandHelp_thenReturnHelpMessage")
                        .jsonFileSource(TEMPLATE_JSON)
                        .phrase("справка")
                        .expectedResponsePhrase("Навык \"Планировщик недели\" предоставляет возможность составления плана событий на неделю, начиная с сегодняшнего дня. Для добавления событий в расписание скажите \"Добавь событие\", далее следуйте инструкциям голосового помощника, время события нужно указывать в двадцатичетырёхчасовом формате, например девять часов, шестнадцать часов тридцать пять минут. Если вы хотите узнать план событий на сегодня скажите \"План на сегодня\". Для того чтобы узнать план на завтра, воспользуйтесь командой \"План на завтра\". Можно узнать план на всю неделю с помощью команды \"План на неделю\". Для выхода из навыка скажите \"Выход\". Версия навыка два ноль ноль")
                        .build())

        );
    }
}