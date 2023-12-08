package com.github.valentina810.weekplannerformarusia.action.handler;

import lombok.Getter;

import java.util.List;

@Getter
public class BaseLoaderHandler {

    private String phrase;
    private String messagePositive;
    private String messageNegative;
    private String operation;
    private List<BaseLoaderHandler> actions;
}