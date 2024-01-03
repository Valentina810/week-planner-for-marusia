package com.github.valentina810.weekplannerformarusia.storage.session;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Builder
@EqualsAndHashCode
public class Actions {
    @Getter
    private List<PrevAction> prevActions;
}