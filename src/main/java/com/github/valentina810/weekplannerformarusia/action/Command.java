package com.github.valentina810.weekplannerformarusia.action;

import lombok.Getter;

import java.util.List;

@Getter
public class Command {
    private TypeAction operation;
    private Boolean isSimple;
    private TypeAction prevOperation;
    private String messagePositive;
    private String messageNegative;
    private List<Command> actions;
}