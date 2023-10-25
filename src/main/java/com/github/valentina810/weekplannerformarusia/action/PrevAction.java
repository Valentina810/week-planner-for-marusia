package com.github.valentina810.weekplannerformarusia.action;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
/**
 *  Активность
 *  typeAction - код активности
 * valueAction - атрибут активности, например время или название
 */
public class PrevAction {
    private TypeAction typeAction;
    private String valueAction;
}