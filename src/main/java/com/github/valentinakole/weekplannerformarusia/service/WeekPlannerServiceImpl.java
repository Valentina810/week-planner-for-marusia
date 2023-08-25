package com.github.valentinakole.weekplannerformarusia.service;

import com.github.valentinakole.weekplannerformarusia.model.DayWeek;
import com.github.valentinakole.weekplannerformarusia.model.MarusiaResponse;
import com.github.valentinakole.weekplannerformarusia.model.PersistentStorage;
import com.github.valentinakole.weekplannerformarusia.model.Response;
import com.github.valentinakole.weekplannerformarusia.model.Session;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Slf4j
@Service
public class WeekPlannerServiceImpl implements WeekPlannerService {
    @Override
    public ResponseEntity getResponse(Object object) {
        String body = new Gson().toJson(getResponseObject(object));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://skill-debugger.marusia.mail.ru");
        headers.add("Content-Type", "application/json");
        log.info("Сформирован ответ {}", body);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    /**
     * Получить из входного объекта данные для ответа
     *
     * @param object -  входной запрос
     * @return - ответ
     */
    private MarusiaResponse getResponseObject(Object object) {
        MarusiaResponse marusiaResponse;
        try {
            String jsonString = new Gson().toJson(object);
            JSONObject jsonObject = new JSONObject(jsonString);
            String escapedPhrase = new JSONObject(jsonString)
                    .getJSONObject("request").getString("original_utterance")
                    .replace("\"", "").replace("\\\"", "");
            String responsePhrase;

            switch (escapedPhrase) {
                case "Расскажи план на неделю":
                    responsePhrase = "Сейчас расскажу все ваши планы на неделю";
                    break;
                case "Добавь событие":
                    responsePhrase = "На какой день добавить событие?";
                    break;
                case "Расскажи план на сегодня":
                    responsePhrase = "Сегодня нас ждет много интересного";
                    break;
                case "Расскажи план на завтра":
                    responsePhrase = "Завтра будет план точно";
                    break;
                case "Справка":
                    responsePhrase = "Доступные команды: Добавить событие , Расскажи план на сегодня , Расскажи план на завтра , Расскажи план на неделю";
                    break;
                default:
                    responsePhrase = "Выбрана незвестная команда";
            }

            DayWeek[] dayWeeks = getDayWeeks(jsonObject);
            marusiaResponse = MarusiaResponse.builder()
                    .response(Response.builder()
                            .text(responsePhrase)
                            .tts(responsePhrase)
                            .end_session(false).build())
                    .session(Session.builder()
                            .user_id(jsonObject.getJSONObject("session").getString("user_id"))
                            .session_id(jsonObject.getJSONObject("session").getString("session_id"))
                            .message_id(jsonObject.getJSONObject("session").getInt("message_id")).build())
                    .version(jsonObject.getString("version"))
                    .user_state_update(PersistentStorage.builder().data(dayWeeks).build())
                    .build();
        } catch (JSONException e) {
            log.info("Возникла ошибка в процессе распарсивания запроса {}", e.getMessage());
            return null;
        }
        return marusiaResponse;
    }

    private DayWeek[] getDayWeeks(JSONObject jsonObject) {
        DayWeek[] dayWeeks = new DayWeek[7];
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("state").getJSONObject("user").getJSONArray("data");
            for (int i = 0; i < 7; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                dayWeeks[i] = DayWeek.builder().date(jsonObject1.getString("date"))
                        .timeTable(jsonArray.getJSONObject(i).getString("timeTable")).build();
            }
        } catch (JSONException e) {
            log.info("Не удалось получить из запроса данные планировщика, ошибка {}", e.getMessage());
            log.info("Выполняем заполнение планировщика данными");
            for (int i = 0; i < 7; i++) {
                dayWeeks[i] = DayWeek.builder().date(new SimpleDateFormat("dd-MM-yyyy")
                                .format(System.currentTimeMillis() + i * 86400000))
                        .timeTable("").build();
            }
        }
        return dayWeeks;
    }
}