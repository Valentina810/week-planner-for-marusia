package com.github.valentina810.weekplannerformarusia.storage.session;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Активность
 * number - номер в порядке прихода n - последняя, 0 - первая
 * typeAction - код активности
 * valueAction - атрибут активности, например время или название
 */
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class PrevAction {
    private Integer number;
    private TypeAction operation;
    private TypeAction prevOperation;
    private String valueAction;
}