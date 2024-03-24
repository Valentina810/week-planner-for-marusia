package com.github.valentina810.weekplannerformarusia;

import com.github.valentina810.weekplannerformarusia.model.response.Response;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.github.valentina810.weekplannerformarusia.model.response.UserResponse;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;

public interface BaseGeneratorUserResponse {
    String BASE_USER_RESPONSE = "{" +
            "\"response\":{" +
            "\"text\":\"text\"," +
            "\"tts\":\"tts\"," +
            "\"end_session\":false" +
            "}," +
            "\"session\":{" +
            "\"user_id\":\"1\"," +
            "\"session_id\":\"1\"," +
            "\"message_id\":1" +
            "}," +
            "\"version\":\"1.0\"," +
            "\"session_state\":{}," +
            "\"user_state_update\":{}" +
            "}";
    String HEADER_KEY = "application/json";
    String HEADER_VALUE = "Content-Type";

    default UserResponse createUserResponse() {
        UserResponse userResponse = new UserResponse();
        userResponse.setResponse(Response.builder()
                .text("text")
                .tts("tts")
                .end_session(false)
                .build());
        userResponse.setSession(Session.builder()
                .user_id("1")
                .session_id("1")
                .message_id(1)
                .build());
        userResponse.setVersion("1.0");
        userResponse.setUser_state_update(new PersistentStorage());
        userResponse.setSession_state(new SessionStorage());
        return userResponse;
    }
}