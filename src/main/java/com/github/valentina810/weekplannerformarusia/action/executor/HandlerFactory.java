package com.github.valentina810.weekplannerformarusia.action.executor;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.action.executor.composite.BaseExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.SIMPLE;

@Component
public class HandlerFactory {
    private final Map<TypeAction, BaseExecutor> baseExecutors;

    public HandlerFactory(List<BaseExecutor> baseExecutors) {
        this.baseExecutors = baseExecutors.stream()
                .collect(Collectors.toMap(BaseExecutor::getType, Function.identity()));
    }

    public BaseExecutor getHandler(TypeAction typeAction) {
        return Optional.ofNullable(baseExecutors.get(typeAction))
                .orElseGet(() -> baseExecutors.get(SIMPLE));
    }
}