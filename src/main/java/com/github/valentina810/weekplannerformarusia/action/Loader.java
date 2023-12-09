package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.action.handler.LoadCommand;
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
    private final Map<String, LoadCommand> baseHandlers;

    public Loader() {
        List<LoadCommand> handler = new ArrayList<>();
        FileReader.loadJsonFromFile("dictionary.json")
                .forEach(e -> {
                    handler.add(new Gson().fromJson(new Gson().toJson(e), LoadCommand.class));
                });
        this.baseHandlers = handler.stream()
                .collect(Collectors.toMap(e -> e.getPhrase().toLowerCase(), Function.identity()));
    }

    public LoadCommand get(String phrase) {
        if (baseHandlers.get(phrase) == null) return baseHandlers.get("custom");
        else return baseHandlers.get(phrase);
    }
}