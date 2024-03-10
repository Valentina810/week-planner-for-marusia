package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.BiFunction;
import java.util.function.Function;

@SpringBootTest
public class BaseTest {

    @Autowired
    protected ActionExecutor actionExecutor;

    /**
     * Получение ответа на запрос в формате String
     */
    protected Function<String, JSONObject> getResponse = (request) ->
    {
        try {
            UserRequest userRequest = new UserRequest();
            userRequest.fillUserRequest(new Gson().fromJson(request, Object.class));
            return new JSONObject(new Gson().toJson(actionExecutor.createUserResponse(userRequest)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * Получение JSONObject param из ответа
     * в state хранятся хранилище сессии и персистентное хранилище пользователя
     */
    protected BiFunction<JSONObject, String, JSONObject> getValue = JSONObject::optJSONObject;

    /**
     * Получить из ответа объект response
     */
    protected Function<JSONObject, JSONObject> getObjectResponse = (response) ->
    {
        try {
            return response.getJSONObject("response");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * Получает из ответа данные из контекста сессии session_state
     *
     * @param response - ответ
     * @return - sessionStorage в формате SessionStorage
     */
    @SneakyThrows
    protected SessionStorage getSessionStorage(Object response) {
        SessionStorage sessionStorage = SessionStorage.builder().build();
        sessionStorage.setActionsInSessionState(getValue.apply(new JSONObject(String.valueOf(response)), "session_state"));
        return sessionStorage;
    }

    /**
     * Получает из ответа данные из персистентного хранилища user_state_update
     *
     * @param response - ответ
     * @return - sessionStorage в формате SessionStorage
     */
    @SneakyThrows
    protected PersistentStorage getPersistentStorage(Object response) {
        PersistentStorage persistentStorage = new PersistentStorage();
        persistentStorage.setWeekStorage(getValue.apply(new JSONObject(String.valueOf(response)), "user_state_update"));
        return persistentStorage;
    }

    /**
     * Получает из тела запроса в формате String данные из персистентного хранилища state -> user
     *
     * @param request - запрос
     * @return - persistentStorage в формате PersistentStorage
     */
    @SneakyThrows
    protected PersistentStorage getPersistentStorage(String request) {
        PersistentStorage persistentStorage = new PersistentStorage();
        persistentStorage.setWeekStorage(getValue
                .apply(getValue
                        .apply(new JSONObject(request), "state"), "user"));
        return persistentStorage;
    }
}