package com.github.valentina810.weekplannerformarusia.dto;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.Getter;

import java.util.List;

@Getter
public class Command {
    private TypeAction operation;
    private Boolean isSimple;
    private TypeAction prevOperation;
    private String messagePositive;
    private String messageNegative;
    private Boolean isEndSession;
    private Boolean isTerminal;
    private List<Command> actions;
}