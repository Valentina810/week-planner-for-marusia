package com.github.valentina810.weekplannerformarusia.action;

import lombok.Getter;

import java.util.List;

@Getter
public class Token {
    private TypeAction operation;
    private List<PhraseTokens> tokens;
}