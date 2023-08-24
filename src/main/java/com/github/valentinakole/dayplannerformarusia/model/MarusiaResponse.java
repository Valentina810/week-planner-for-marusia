package com.github.valentinakole.dayplannerformarusia.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MarusiaResponse {
    private Response response;
    private Session session;
    private String version;
}