package com.github.valentina810.weekplannerformarusia.storage.session;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@EqualsAndHashCode
@ToString
public class Actions {
    @Getter
    private List<PrevAction> prevActions;
}