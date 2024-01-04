package com.github.valentina810.weekplannerformarusia.action.handler.template;

import com.github.valentina810.weekplannerformarusia.action.handler.ParametersHandler;

import java.util.function.UnaryOperator;

/**
 * Составной обработчик, который:
 * - может добавлять данные в session_state и user_state_update
 * - может содержать простые и сложные вложенные команды
 * - для формирования ответа использует метод actionExecute
 */
public class CompositeHandler extends SimpleHandler {

    private final UnaryOperator<ParametersHandler> actionExecute;

    public CompositeHandler(ParametersHandler parametersHandler, UnaryOperator<ParametersHandler> actionExecute) {
        super(parametersHandler);
        this.actionExecute = actionExecute;
    }

    @Override
    public void execute() {
        super.setDefaultValueParameters(parametersHandler);
        parametersHandler = actionExecute.apply(parametersHandler);
    }
}