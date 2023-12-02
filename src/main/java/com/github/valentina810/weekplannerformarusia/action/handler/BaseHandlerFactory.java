package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BaseHandlerFactory {

    private final Map<TypeAction, BaseHandler> baseHandlers;

    public BaseHandlerFactory(List<BaseHandler> baseHandlers) {
        this.baseHandlers = baseHandlers.stream()
                .collect(Collectors.toMap(BaseHandler::getType, Function.identity()));
    }

    public BaseHandler getByBaseHandlerResponsePhraseType(TypeAction typeAction) {
        return Optional.of(baseHandlers.get(typeAction))
                .orElseThrow(() -> new RuntimeException("Отсутствует реализация для " + typeAction));
    }
}