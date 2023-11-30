package com.github.valentina810.weekplannerformarusia.action.phrase;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.EXIT;

@Component
@RequiredArgsConstructor
public class ExitPhrase implements BasePhrase {
    @Override
    public String getPhrase() {
        return "пока";
    }

    @Override
    public TypeAction getAction() {
        return EXIT;
    }
}