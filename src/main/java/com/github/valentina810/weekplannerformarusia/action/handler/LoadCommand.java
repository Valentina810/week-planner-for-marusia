package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.Getter;

import java.util.List;

@Getter
public class LoadCommand {

    private String phrase;
    private String messagePositive;
    private String messageNegative;
    private TypeAction operation;
    private List<LoadCommand> actions;
}