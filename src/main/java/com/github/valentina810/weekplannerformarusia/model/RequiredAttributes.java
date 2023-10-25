package com.github.valentina810.weekplannerformarusia.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RequiredAttributes {
    private String userId;
    private String sessionId;
    private Integer messageId;
    private String version;
}