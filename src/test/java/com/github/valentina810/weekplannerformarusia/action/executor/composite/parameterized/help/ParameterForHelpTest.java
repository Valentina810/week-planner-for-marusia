package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.help;

import com.github.valentina810.weekplannerformarusia.storage.session.Actions;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ParameterForHelpTest {
    private String testName;
    private String jsonFileSource;
    private String phrase;
    private String prevActions;
    private String expectedResponsePhrase;
    private Actions expectedActions;
}
