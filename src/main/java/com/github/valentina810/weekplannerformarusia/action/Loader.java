package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.action.handler.BaseLoaderHandler;
import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Component
public class Loader {
    private final Map<String, BaseLoaderHandler> baseHandlers;

    public Loader() {
        List<BaseLoaderHandler> handler = new ArrayList<>();
        FileReader.loadJsonFromFile("dictionary.json")
                .forEach(e -> {
                    handler.add(new Gson().fromJson(new Gson().toJson(e), BaseLoaderHandler.class));
                });
        this.baseHandlers = handler.stream()
                .collect(Collectors.toMap(e -> e.getPhrase().toLowerCase(), Function.identity()));
    }
}