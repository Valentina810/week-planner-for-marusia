package com.github.valentinakole.dayplannerformarusia.service;

import com.github.valentinakole.dayplannerformarusia.model.MarusiaResponse;
import com.github.valentinakole.dayplannerformarusia.model.Response;
import com.github.valentinakole.dayplannerformarusia.model.Session;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Slf4j
@Service
public class VoiceAssistantServiceImpl implements VoiceAssistantService {
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
            marusiaResponse = MarusiaResponse.builder()
                    .response(Response.builder()
                            .text(escapedPhrase)
                            .tts(String.format(String.format(escapedPhrase + " текущее время " +
                                    new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()))))
                            .end_session(false).build())
                    .session(Session.builder()
                            .user_id(jsonObject.getJSONObject("session").getString("user_id"))
                            .session_id(jsonObject.getJSONObject("session").getString("session_id"))
                            .message_id(jsonObject.getJSONObject("session").getInt("message_id")).build())
                    .version(jsonObject.getString("version"))
                    .build();
        } catch (JSONException e) {
            log.info("Возникла ошибка в процессе распарсивания запроса {}", e.getMessage());
            return null;
        }
        return marusiaResponse;
    }
}