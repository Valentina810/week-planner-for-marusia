package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.dto.Token;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class TokenLoader {
    @Getter
    private List<Token> tokens;

    @PostConstruct
    public void loadTokens() {
        log.info("Загрузка токенов из файла tokens.json");
        tokens = FileReader.loadJsonFromFile("tokens.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Token.class))
                .collect(Collectors.toList());
    }
}