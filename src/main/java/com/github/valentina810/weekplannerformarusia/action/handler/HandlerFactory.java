package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HandlerFactory {
    private final Map<TypeAction, BaseHandler> baseHandlers;

    public HandlerFactory(List<BaseHandler> baseHandlers) {
        this.baseHandlers = baseHandlers.stream()
                .collect(Collectors.toMap(BaseHandler::getType, Function.identity()));
    }

    public BaseHandler getByBaseHandlerResponsePhraseType(TypeAction typeAction) {
        if (typeAction == null) {
            return baseHandlers.get(TypeAction.UNKNOWN);
        } else
            return baseHandlers.get(typeAction) != null ? baseHandlers.get(typeAction) : baseHandlers.get(TypeAction.UNKNOWN);
    }
}