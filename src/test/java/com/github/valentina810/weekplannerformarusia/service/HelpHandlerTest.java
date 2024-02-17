package com.github.valentina810.weekplannerformarusia.service;

import com.github.valentina810.weekplannerformarusia.model.request.UserRequest;
import com.github.valentina810.weekplannerformarusia.service.parameterized.help.ParameterForHelpTest;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class HelpHandlerTest {

    @Autowired
    private WeekPlannerService weekPlannerService;

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("com.github.valentina810.weekplannerformarusia.service.parameterized.help.HelpTestData#providerHelpTest")
    public void checkCommandHelp(ParameterForHelpTest parameterForHelpTest) {
        String json = FileReader.loadStringFromFile(parameterForHelpTest.getJsonFileSource())
                .replace("phrase", parameterForHelpTest.getPhrase())
                .replace("prevAction", parameterForHelpTest.getPrevActions());
        Object response = weekPlannerService.getResponse(new Gson().fromJson(json, UserRequest.class)).getBody();
        JSONObject responseObject = new JSONObject(String.valueOf(response)).getJSONObject("response");

        assertAll(
                () -> assertEquals(parameterForHelpTest.getExpectedResponsePhrase(), responseObject.getString("text")),
                () -> assertFalse(responseObject.getBoolean("end_session")),
                () -> assertEquals(parameterForHelpTest.getExpectedActions(), getSessionStorage(response).getActions())
        );
    }

    /**
     * Получает из запроса данные из контекста сессии
     *
     * @param response - запрос
     * @return - sessionStorage в формате SessionStorage
     */
    @SneakyThrows
    private SessionStorage getSessionStorage(Object response) {
        SessionStorage sessionStorage = SessionStorage.builder().build();
        sessionStorage.setActionsInSessionState(new JSONObject(String.valueOf(response)).optJSONObject("session_state"));
        return sessionStorage;
    }
}