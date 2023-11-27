package com.github.valentina810.weekplannerformarusia.action.phrase;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.HELP;

@Component
@RequiredArgsConstructor
public class HelpPhrase implements BasePhrase {
    @Override
    public String getPhrase() {
        return "справка";
    }

    @Override
    public TypeAction getAction() {
        return HELP;
    }
}