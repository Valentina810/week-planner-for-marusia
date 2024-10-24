package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.ActionExecutor;
import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.dayplan.ParameterForEventsForDateTest;
import com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help.ParameterWithPrevActionsTest;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.storage.session.SessionStorage;
import com.github.valentina810.weekplannerformarusia.util.FileReader;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.format.TextStyle.FULL;

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
            return new JSONObject(new Gson().toJson(actionExecutor.createUserResponse(new Gson().fromJson(request, Object.class))));
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
        SessionStorage sessionStorage = new SessionStorage();
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

    protected static String getRequestFromFile(String jsonFileSource, String phrase) {
        return FileReader
                .loadStringFromFile(jsonFileSource)
                .replace("phrase", phrase);
    }

    /**
     * Получение "тела" запроса из файла
     */
    protected static String getRequestFromFile(ParameterWithPrevActionsTest parameterWithPrevActionsTest) {
        return FileReader
                .loadStringFromFile(parameterWithPrevActionsTest.getJsonFileSource())
                .replace("phrase", parameterWithPrevActionsTest.getPhrase())
                .replace("prevAction", parameterWithPrevActionsTest.getPrevActions());
    }

    /**
     * Получение "тела" запроса из файла
     */
    protected static String getRequestFromFile(ParameterForEventsForDateTest parameterForEventsForDateTest) {
        return FileReader.loadStringFromFile(parameterForEventsForDateTest.getJsonFileSource())
                .replace("testDate", parameterForEventsForDateTest.getDate())
                .replace("testEvents", parameterForEventsForDateTest.getTodayEvents())
                .replace("phrase", parameterForEventsForDateTest.getPhrase());
    }

    /**
     * Получить из тела запроса таймзону в формате "Europe/Moscow"
     */
    @SneakyThrows
    public String getTimeZoneFromFile(String file) {
        return getValue.apply(new JSONObject(file), "meta").getString("timezone");
    }

    public static String getNextDayOfWeek(String dayOfWeekName, String timezone) {
        LocalDate today = ZonedDateTime.now(ZoneId.of(timezone)).toLocalDate();
        return Stream.of(DayOfWeek.values())
                .filter(day -> dayOfWeekName.contains(day.getDisplayName(FULL, new Locale("ru"))))
                .findFirst()
                .map(targetDay -> {
                    int daysUntilNextTarget = (targetDay.getValue() - today.getDayOfWeek().getValue() + 7) % 7;
                    return daysUntilNextTarget == 0 ? today : today.plusDays(daysUntilNextTarget);
                })
                .orElse(today)
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}