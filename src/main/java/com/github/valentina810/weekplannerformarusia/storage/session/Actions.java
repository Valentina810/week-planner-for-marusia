package com.github.valentina810.weekplannerformarusia.storage.session;

import lombok.Builder;
import lombok.Getter;

import java.util.TreeSet;

@Builder
public class Actions {
    @Getter
    private TreeSet<PrevAction> actions;
}