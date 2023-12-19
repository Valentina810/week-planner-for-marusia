package com.github.valentina810.weekplannerformarusia.storage.session;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class Actions {
    @Getter
    private List<PrevAction> prevActions;
}