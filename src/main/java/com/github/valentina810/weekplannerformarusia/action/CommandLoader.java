package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Component
public class CommandLoader {
    private final Map<String, Command> loadCommands;

    public CommandLoader() {
        this.loadCommands = FileReader.loadJsonFromFile("dictionary.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Command.class))
                .collect(Collectors.toMap(e -> e.getPhrase().toLowerCase(), Function.identity()));
    }


    public Command get(String phrase) {
        return loadCommands.getOrDefault(phrase, loadCommands.get("custom"));
    }

    public Command get(TypeAction operation) {
        return loadCommands.values().stream()
                .filter(e -> e.getOperation().equals(operation))
                .findFirst().orElse(loadCommands.get("custom"));
    }
}