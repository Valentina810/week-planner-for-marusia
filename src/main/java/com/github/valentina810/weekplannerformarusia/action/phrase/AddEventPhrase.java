package com.github.valentina810.weekplannerformarusia.action.phrase;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.github.valentina810.weekplannerformarusia.action.TypeAction.ADD_EVENT;

@Component
@RequiredArgsConstructor
public class AddEventPhrase implements BasePhrase {
    @Override
    public String getPhrase() {
        return "добавь событие";
    }

    @Override
    public TypeAction getAction() {
        return ADD_EVENT;
    }
}