package com.github.valentina810.weekplannerformarusia.action.phrase;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BasePhraseFactory {
    private final Map<String, TypeAction> basePhrases;

    public BasePhraseFactory(List<BasePhrase> basePhrases) {
        this.basePhrases = new HashMap<>();
        for (BasePhrase phrase : basePhrases) {
            this.basePhrases.put(phrase.getPhrase(), phrase.getAction());
        }
    }

    public TypeAction getBasePhrase(String phrase) {
        TypeAction typeAction = basePhrases.get(phrase);
        return typeAction != null ? typeAction : TypeAction.NONE;
    }
}