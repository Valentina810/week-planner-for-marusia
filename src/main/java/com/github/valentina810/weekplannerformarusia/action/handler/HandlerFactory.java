package com.github.valentina810.weekplannerformarusia.action.handler;

import com.github.valentina810.weekplannerformarusia.action.LoadCommand;
import com.github.valentina810.weekplannerformarusia.action.handler.handler.CompositeHandler;
import com.github.valentina810.weekplannerformarusia.action.handler.handler.SimpleHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component
@Slf4j
public class HandlerFactory {
    /**
     * Определяет метод execute для
     * конкретной команды
     *
     * @param loadCommand
     * @return
     */
    public SimpleHandler getHandler(LoadCommand loadCommand) {
        if (loadCommand.getIsSimple()) {
            return new SimpleHandler(ParametersHandler.builder().loadCommand(loadCommand).build());
        } else {
            UnaryOperator<ParametersHandler> actionExecute;
            CompositeHandlersActionExecute compositeHandlersActionExecute = new CompositeHandlersActionExecute();
            switch (loadCommand.getOperation()) {
                case WEEKLY_PLAN:
                    actionExecute = compositeHandlersActionExecute.getWeeklyPlan();
                    break;
                case TODAY_PLAN:
                    actionExecute = compositeHandlersActionExecute.getTodayPlan();
                    break;
                case TOMORROW_PLAN:
                    actionExecute = compositeHandlersActionExecute.getTomorrowPlan();
                    break;
//                case ADD_EVENT:
//                    break;
//                case ADD_DAY:
//                    break;
//                case ADD_TIME:
//                    break;
//                case ADD_NAME:
//                    break;
                case HELP:
                    actionExecute = compositeHandlersActionExecute.getHelp();
                    break;
                case EXIT:
                    actionExecute = compositeHandlersActionExecute.getExit();
                    break;
                case EXIT_MAIN_MENU:
                    actionExecute = compositeHandlersActionExecute.getExitMainMenu();
                    break;
                case UNKNOWN:
                    return new SimpleHandler(ParametersHandler.builder().loadCommand(loadCommand).build());
                default:
                    actionExecute = compositeHandlersActionExecute.getExit();
            }
            return new CompositeHandler(ParametersHandler.builder().loadCommand(loadCommand).build(), actionExecute);
        }
    }
}