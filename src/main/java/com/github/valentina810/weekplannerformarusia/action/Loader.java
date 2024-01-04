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
public class Loader {
    private final Map<String, LoadCommand> loadCommands;

    public Loader() {
        List<LoadCommand> handler = new ArrayList<>();
        FileReader.loadJsonFromFile("dictionary.json")
                .forEach(e -> handler.add(new Gson().fromJson(new Gson().toJson(e), LoadCommand.class)));
        this.loadCommands = handler.stream()
                .collect(Collectors.toMap(e -> e.getPhrase().toLowerCase(), Function.identity()));
    }

    public LoadCommand get(String phrase) {
        return loadCommands.get(phrase) == null ? loadCommands.get("custom") : loadCommands.get(phrase);
    }

    public LoadCommand get(TypeAction operation) {
        return loadCommands.values().stream()
                .filter(e -> e.getOperation().equals(operation))
                .findFirst().orElse(loadCommands.get("custom"));
    }
}