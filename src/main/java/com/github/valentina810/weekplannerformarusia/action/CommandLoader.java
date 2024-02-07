package com.github.valentina810.weekplannerformarusia.action;

import com.github.valentina810.weekplannerformarusia.FileReader;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Component
public class CommandLoader {
    private static Map<TypeAction, Command> loadCommands;

    @PostConstruct
    public void loadCommands() {
        log.info("Загрузка комманд из файла commands.json");
        loadCommands = FileReader.loadJsonFromFile("commands.json").asList()
                .stream()
                .map(json -> new Gson().fromJson(new Gson().toJson(json), Command.class))
                .collect(Collectors.toMap(Command::getOperation, Function.identity()));
    }

    public static Command get(TypeAction operation) {
        return Optional.ofNullable(findCommandByOperation(loadCommands.values().stream().toList(), operation))
                .orElse(new Command());
    }

    private static Command findCommandByOperation(List<Command> commands, TypeAction targetOperation) {
        if (commands == null || commands.isEmpty()) {
            return null;
        }
        for (Command command : commands) {
            if (command.getOperation() == targetOperation) {
                return command;
            }
            Command nestedResult = findCommandByOperation(command.getActions(), targetOperation);
            if (nestedResult != null) {
                return nestedResult;
            }
        }
        return null;
    }

    public static List<Command> findCommandsByPrevOperation(TypeAction prevOperation) {
        List<Command> result = new ArrayList<>();
        for (Command command : loadCommands.values()) {
            findCommandsRecursively(command, prevOperation, result);
        }
        return result;
    }

    private static void findCommandsRecursively(Command currentCommand, TypeAction prevOperation, List<Command> result) {
        if (currentCommand.getPrevOperation() != null && currentCommand.getPrevOperation().equals(prevOperation)) {
            result.add(currentCommand);
        }
        if (currentCommand.getActions() != null) {
            for (Command nestedCommand : currentCommand.getActions()) {
                findCommandsRecursively(nestedCommand, prevOperation, result);
            }
        }
    }
}