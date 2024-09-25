package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.hello;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class HelloTestData {
    private static final String TEMPLATE_JSON = "action/plantodate/PlanEmpty.json";
    private static final String EXPECTED_PHRASE = "Вас приветствует навык \"Планировщик недели\"! Навык предоставляет возможность составить план событий на неделю, начиная с сегодняшнего дня. Для добавления событий в расписание скажите \"Добавь событие\", далее следуйте инструкциям. Если вы хотите узнать план событий на сегодня скажите \"План на сегодня\". Для того чтобы узнать план на завтра, воспользуйтесь командой \"План на завтра\". Можно узнать план на всю неделю с помощью команды \"План на неделю\". Для выхода из навыка скажите \"Выход\". Если у вас остались вопросы, скажите \"Справка\" для подробного объяснения работы навыка";


    static Stream<Arguments> providerHelloTest() {

        return Stream.of(
                of(
                        ParameterHelloTestDataTest.builder()
                                .testName("getHelloMessage_whenCallActivationPhrase1_thenReturnHelloMessage")
                                .phrase("планировщик")
                                .jsonRequest(TEMPLATE_JSON)
                                .expectedPhrase(EXPECTED_PHRASE)
                                .isEndSession(false)
                                .build()
                ),
                of(
                        ParameterHelloTestDataTest.builder()
                                .testName("getHelloMessage_whenCallActivationPhrase2_thenReturnHelloMessage")
                                .phrase("планировщик недели")
                                .jsonRequest(TEMPLATE_JSON)
                                .expectedPhrase(EXPECTED_PHRASE)
                                .isEndSession(false)
                                .build()
                ));
    }
}