package com.github.valentina810.weekplannerformarusia.storage.session;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
/**
 *  Активность
 *  number - номер в порядке прихода n - последняя, 0 - первая
 *  typeAction - код активности
 *  valueAction - атрибут активности, например время или название
 */
public class PrevAction implements Comparable<PrevAction> {
    private Integer number;
    private TypeAction typeAction;
    private String valueAction;


    @Override
    public int compareTo(PrevAction prevAction) {
        return this.number - prevAction.number;
    }
}