package com.github.valentinakole.weekplannerformarusia.model;

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
    private PersistentStorage user_state_update;
}