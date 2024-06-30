package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.hello;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class HelloTestData {
    private static final String TEMPLATE_JSON = "action/plantodate/PlanEmpty.json";

    static Stream<Arguments> providerHelloTest() {

        return Stream.of(
                of(
                        ParameterHelloTestDataTest.builder()
                                .testName("getHelloMessage_whenCallActivationPhrase1_thenReturnHelloMessage")
                                .phrase("планировщик")
                                .jsonRequest(TEMPLATE_JSON)
                                .expectedPhrase("Вас приветствует навык Планировщик недели! У меня есть команды: план на неделю, план на сегодня, план на завтра, добавь событие, справка и выход")
                                .isEndSession(false)
                                .build()
                ),
                of(
                        ParameterHelloTestDataTest.builder()
                                .testName("getHelloMessage_whenCallActivationPhrase2_thenReturnHelloMessage")
                                .phrase("планировщик недели")
                                .jsonRequest(TEMPLATE_JSON)
                                .expectedPhrase("Вас приветствует навык Планировщик недели! У меня есть команды: план на неделю, план на сегодня, план на завтра, добавь событие, справка и выход")
                                .isEndSession(false)
                                .build()
                ));
    }
}