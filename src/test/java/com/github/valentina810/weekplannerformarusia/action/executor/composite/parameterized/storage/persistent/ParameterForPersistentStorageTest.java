package com.github.valentina810.weekplannerformarusia.action.executor.composite.parameterized.storage.persistent;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class ParameterForPersistentStorageTest {
    private String eventDay;
    private String timeZone;
}
