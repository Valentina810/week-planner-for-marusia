package com.github.valentina810.weekplannerformarusia.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Сущность-ответ
 * Ответ на фразу пользователя в необходимой структуре
 */
@Slf4j
@Getter
@Setter
@Component
@RequiredArgsConstructor
public class UserResponse {
    private Response response;
    private Session session;
    private String version;
    private Object session_state;
    private Object user_state_update;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }
}