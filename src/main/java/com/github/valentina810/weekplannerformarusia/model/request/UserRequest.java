package com.github.valentina810.weekplannerformarusia.model.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.valentina810.weekplannerformarusia.model.JSONObjectSerializer;
import com.github.valentina810.weekplannerformarusia.model.response.Session;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Сущность, которая представляет запрос, который пришел от пользователя
 */
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class UserRequest {
    private Request request;
    private Session session;
    private State state;
    private String version;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JSONObject.class, new JSONObjectSerializer());
        objectMapper.registerModule(module);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            log.error("Возникла ошибка при попытке преобразовать UserRequest в строку json "+e.getMessage());
            return "{}";
        }
    }

    public void fillUserRequest(Object object) {
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(object));
            request = Request.builder().command(jsonObject.getJSONObject("request").getString("command")).build();
            session = Session.builder()
                    .user_id(jsonObject.getJSONObject("session").getString("user_id"))
                    .session_id(jsonObject.getJSONObject("session").getString("session_id"))
                    .message_id(jsonObject.getJSONObject("session").getInt("message_id")).build();
            state = State.builder()
                    .session(jsonObject.getJSONObject("state").getJSONObject("session"))
                    .user(jsonObject.getJSONObject("state").getJSONObject("user"))
                    .build();
            version = jsonObject.getString("version");
        } catch (JSONException e) {
            log.info("Возникла ошибка {} при получении атрибутов из входящего запроса", e.getMessage());
        }
    }
}