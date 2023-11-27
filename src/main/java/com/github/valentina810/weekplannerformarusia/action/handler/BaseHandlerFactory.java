package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BaseHandlerFactory {

    private final Map<TypeAction, BaseHandler> baseHandlers;

    public BaseHandlerFactory(List<BaseHandler> baseHandlers) {
        this.baseHandlers = new HashMap<>();
        for (BaseHandler phrase : baseHandlers) {
            this.baseHandlers.put(phrase.getType(), phrase);
        }
    }

    public BaseHandler getByBaseHandlerResponsePhraseType(TypeAction typeAction) {
        BaseHandler handler = baseHandlers.get(typeAction);
        if (handler == null) {
            throw new RuntimeException("Отсутствует реализация для " + typeAction);
        }
        return handler;
    }
}