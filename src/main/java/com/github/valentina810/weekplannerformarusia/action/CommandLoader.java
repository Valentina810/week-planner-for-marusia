package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.google.gson.Gson;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class CommandLoader {
    private final Map<TypeAction, Command> loadCommands;

    public CommandLoader() {
        this.loadCommands = FileReader.loadJsonFromFile("commands.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Command.class))
                .collect(Collectors.toMap(e -> e.getOperation(), Function.identity()));
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