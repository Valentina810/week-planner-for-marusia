package com.github.valentina810.weekplannerformarusia.action.handler.helphandlerchild;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandHelpHandler implements ChildHandler {

    @Override
    public String find(String command) {
        return switch (command) {
            case "команды" -> "Вот какие у меня есть команды: план на неделю, план на сегодня, план на завтра";
            case "как добавить событие" -> "для того чтобы добавить событие скажите добавь событие";
            case "об авторе" -> "имя автора Валентина";
            default -> "команда не распознана";
        };
    }
}