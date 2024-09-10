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
                        .expectedResponsePhrase("Навык Планировщик недели предоставляет возможность составления плана событий на неделю, начиная с сегодняшнего дня. Для добавления событий в расписание скажите добавь событие, далее следуйте инструкциям голосового помошника, время события нужно указывать в двадцатичетырёхчасовом формате, например девять ноль ноль, шестнадцать тридцать пять. Если вы хотите узнать план событий на сегодня скажите план на сегодня. Для того чтобы узнать план на завтра, воспользуйтесь командой план на завтра. Можно узнать план на всю неделю с помощью команды план на неделю. Для выхода из навыка скажите выход. Версия навыка два ноль ноль")
                        .build())

        );
    }
}