package com.github.valentina810.weekplannerformarusia.storage.session;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *  Активность
 *  number - номер в порядке прихода n - последняя, 0 - первая
 *  typeAction - код активности
 *  valueAction - атрибут активности, например время или название
 */
@Builder
@Setter
@Getter
public class PrevAction implements Comparable<PrevAction> {
    @SerializedName("number")
    private Integer number;
    @SerializedName("operation")
    private TypeAction operation;
    @SerializedName("prevOperation")
    private TypeAction prevOperation;
    @SerializedName("valueAction")
    private String valueAction;


    @Override
    public int compareTo(PrevAction prevAction) {
        return this.number - prevAction.number;
    }
}