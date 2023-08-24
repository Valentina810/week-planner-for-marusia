package com.github.valentinakole.dayplannerformarusia.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VoiceAssistantServiceImpl implements VoiceAssistantService {
    @Override
    public ResponseEntity getResponse(Object object) {
        String jsonString = new Gson().toJson(object);
        JSONObject jsonObject;
        String phrase = "";
        String version = "";
        String sessionId = "";
        String userId = "";
        Integer messageId = 0;
        String escapedPhrase = "";
        try {
            jsonObject = new JSONObject(jsonString);
            phrase = jsonObject.getJSONObject("request").getString("original_utterance");
            version = jsonObject.getString("version");
            sessionId = jsonObject.getJSONObject("session").getString("session_id");
            userId = jsonObject.getJSONObject("session").getString("user_id");
            messageId = jsonObject.getJSONObject("session").getInt("message_id");
        } catch (JSONException e) {
            log.info("Возникла ошибка в процессе распарсивания запроса {}", e.getMessage());
        }
        escapedPhrase = phrase.replace("\"", "").replace("\\\"", "");
        String body = String.format("{\n" +
                "\t\"response\": {\n" +
                "\"text\": \"%s\",\n" +
                "\"tts\": \"%s\",\n" +
                "\"end_session\": false\n" +
                "\t},\n" +
                "\t\"session\": {\n" +
                "\"user_id\": \"%s\",\n" +
                "\"session_id\": \"%s\",\n" +
                "\"message_id\": %d\n" +
                "\t},\n" +
                "\t\"version\": \"%s\"\n" +
                "\t}", escapedPhrase, escapedPhrase, userId, sessionId, messageId, version);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "https://skill-debugger.marusia.mail.ru");
        headers.add("Content-Type", "application/json");
        log.info("Сформирован ответ {}", body);
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}