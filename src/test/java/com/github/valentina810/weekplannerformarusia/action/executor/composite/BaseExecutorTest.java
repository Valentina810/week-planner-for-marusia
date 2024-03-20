package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class BaseExecutorTest {

    private static final String DATE_FORMATTED = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    private BaseExecutor baseExecutor;

    @Mock
    private Command mockCommand;

    @Mock
    private PersistentStorage mockPersistentStorage;


    @BeforeEach
    void setUp() {
        openMocks(this);
        baseExecutor = new BaseExecutor() {
            @Override
            public TypeAction getType() {
                return null;
            }

            @Override
            public ResponseParameters getResponseParameters(ExecutorParameter executorParameter) {
                return null;
            }
        };
    }

    @Test
    void getEventsForDate_whenNotEmptyEvents_thenReturnPositiveMessageAndEvents() {
        LocalDate date = LocalDate.now();
        when(mockCommand.getMessageNegative()).thenReturn("У вас пока нет событий на {date}");
        when(mockCommand.getMessagePositive()).thenReturn("Ваши события на {date}: ");
        when(mockPersistentStorage.getEventsByDay(DATE_FORMATTED))
                .thenReturn(Collections.singletonList(Event.builder()
                        .time("10:00")
                        .name("Прогулка")
                        .build()));

        String result = baseExecutor.getEventsForDate(mockCommand, date, mockPersistentStorage);

        assertEquals("Ваши события на " + DATE_FORMATTED + ": 10:00 Прогулка", result);
    }

    @Test
    void getEventsForDate_whenEmptyEvents_thenReturnDefaultMessage() {
        LocalDate date = LocalDate.now();
        when(mockCommand.getMessageNegative()).thenReturn("У вас пока нет событий на {date}");
        when(mockPersistentStorage.getEventsByDay(DATE_FORMATTED)).thenReturn(Collections.emptyList());

        String result = baseExecutor.getEventsForDate(mockCommand, date, mockPersistentStorage);

        assertEquals("У вас пока нет событий на " + DATE_FORMATTED, result);
    }

    @Test
    void getEventsForDate_whenNullDefaultMessage_thenReturnEmptyString() {
        LocalDate date = LocalDate.now();
        when(mockCommand.getMessageNegative()).thenReturn(null);

        String result = baseExecutor.getEventsForDate(mockCommand, date, mockPersistentStorage);

        assertEquals("", result);
    }
}