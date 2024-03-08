package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.dto.Token;
import com.github.valentina810.weekplannerformarusia.util.JsonFileReader;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class TokenLoader {
    @Getter
    private List<Token> tokens;

    @PostConstruct
    public void loadTokens() {
        log.info("Загрузка токенов из файла tokens.json");
        try {
            tokens = JsonFileReader.readJsonArrayFromFile("tokens.json").asList()
                    .stream()
                    .map(json -> new Gson().fromJson(new Gson().toJson(json), Token.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(getStackTrace(e));
        }
    }

    private static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}