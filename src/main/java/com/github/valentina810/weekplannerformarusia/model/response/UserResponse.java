package com.github.valentina810.weekplannerformarusia.model.response;

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
}