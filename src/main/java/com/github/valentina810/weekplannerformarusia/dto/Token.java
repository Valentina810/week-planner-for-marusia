package com.github.valentina810.weekplannerformarusia.dto;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.Getter;

import java.util.List;

@Getter
public class Token {
    private TypeAction operation;
    private List<PhraseTokens> tokens;
}