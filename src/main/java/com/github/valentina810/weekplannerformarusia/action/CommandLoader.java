package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
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
public class CommandLoader {
    private final Map<String, Command> loadCommands;

    public CommandLoader() {
        List<Command> commands = new ArrayList<>();
        FileReader.loadJsonFromFile("dictionary.json")
                .forEach(e -> commands.add(new Gson().fromJson(new Gson().toJson(e), Command.class)));
        this.loadCommands = commands.stream()
                .collect(Collectors.toMap(e -> e.getPhrase().toLowerCase(), Function.identity()));
    }

    public Command get(String phrase) {
        return loadCommands.get(phrase) == null ? loadCommands.get("custom") : loadCommands.get(phrase);
    }

    public Command get(TypeAction operation) {
        return loadCommands.values().stream()
                .filter(e -> e.getOperation().equals(operation))
                .findFirst().orElse(loadCommands.get("custom"));
    }
}