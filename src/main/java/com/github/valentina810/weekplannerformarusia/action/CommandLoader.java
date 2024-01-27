package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.UNKNOWN;

@Getter
@Slf4j
@Component
public class CommandLoader {
    private static Map<TypeAction, Command> loadCommands;

    public CommandLoader() {
        log.info("Загрузка комманд из файла commands.json");
        loadCommands = FileReader.loadJsonFromFile("commands.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Command.class))
                .collect(Collectors.toMap(e -> e.getOperation(), Function.identity()));
    }

    public static Command get(TypeAction operation) {
        return loadCommands.values().stream()
                .filter(e -> e.getOperation().equals(operation))
                .findFirst().orElse(loadCommands.get(UNKNOWN));
    }
}