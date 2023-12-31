package com.github.valentina810.weekplannerformarusia.action;

import lombok.Getter;

import java.util.List;

@Getter
public class LoadCommand {

    private String phrase;
    private Boolean isSimple;
    private TypeAction prevOperation;
    private String messagePositive;
    private String messageNegative;
    private TypeAction operation;
    private List<LoadCommand> actions;
}