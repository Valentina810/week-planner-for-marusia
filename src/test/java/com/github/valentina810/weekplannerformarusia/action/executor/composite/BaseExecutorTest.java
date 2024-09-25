package com.github.valentina810.weekplannerformarusia.action.executor.composite;

import com.github.valentina810.weekplannerformarusia.action.TypeAction;
import com.github.valentina810.weekplannerformarusia.dto.Command;
import com.github.valentina810.weekplannerformarusia.dto.ExecutorParameter;
import com.github.valentina810.weekplannerformarusia.dto.ResponseParameters;
import com.github.valentina810.weekplannerformarusia.storage.persistent.Event;
import com.github.valentina810.weekplannerformarusia.storage.persistent.PersistentStorage;
import com.github.valentina810.weekplannerformarusia.util.DateConverter;
import com.github.valentina810.weekplannerformarusia.util.Formatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseExecutorTest {

    private static final LocalDate date = LocalDate.now();

    private static final String DATE_FORMATTED = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    private BaseExecutor baseExecutor;

    @Mock
    private Command mockCommand;

    @Mock
    private PersistentStorage mockPersistentStorage;

    @BeforeEach
    void setUp() {
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
    void getEventsForDate_whenEventsInTheList_thenReturnPositiveMessageAndListOfEvents() {
        when(mockCommand.getMessageNegative()).thenReturn("У вас пока нет событий на {date}");
        when(mockCommand.getMessagePositive()).thenReturn("Ваши события на {date}: ");
        when(mockPersistentStorage.getEventsByDay(DATE_FORMATTED))
                .thenReturn(Collections.singletonList(Event.builder()
                        .time("10:00")
                        .name("Прогулка")
                        .build()));

        String result = baseExecutor.getEventsForDate(mockCommand, date, mockPersistentStorage);

        assertEquals("Ваши события на " + DateConverter.convertDate.apply(Formatter.convertStringToDate.apply(DATE_FORMATTED)) + ": 10:00 Прогулка", result);
        checkSuccessfulCallMethods();
    }

    private void checkSuccessfulCallMethods() {
        InOrder inOrder = inOrder(mockCommand, mockPersistentStorage);
        inOrder.verify(mockCommand, times(1)).getMessageNegative();
        inOrder.verify(mockCommand, times(1)).getMessagePositive();
        inOrder.verify(mockPersistentStorage, times(1)).getEventsByDay(DATE_FORMATTED);
        verifyNoMoreInteractions(mockPersistentStorage, mockCommand);
    }

    @Test
    void getEventsForDate_whenNoEventsInWeekStorage_thenReturnNegativeMessage() {
        when(mockCommand.getMessageNegative()).thenReturn("У вас пока нет событий на {date}");
        when(mockPersistentStorage.getEventsByDay(DATE_FORMATTED)).thenReturn(Collections.emptyList());

        String result = baseExecutor.getEventsForDate(mockCommand, date, mockPersistentStorage);

        assertEquals("У вас пока нет событий на " + DateConverter.convertDate.apply(Formatter.convertStringToDate.apply(DATE_FORMATTED)), result);
        checkSuccessfulCallMethods();
    }

    @Test
    void getEventsForDate_whenCommandIsNotSetNegativeMessage_theReturnEmptyString() {
        when(mockCommand.getMessageNegative()).thenReturn(null);

        String result = baseExecutor.getEventsForDate(mockCommand, date, mockPersistentStorage);

        assertEquals("", result);
        checkUnsuccessfulCallMethods();
    }

    private void checkUnsuccessfulCallMethods() {
        verify(mockCommand, times(1)).getMessageNegative();
        verify(mockCommand, never()).getMessagePositive();
        verify(mockPersistentStorage, never()).getEventsByDay(DATE_FORMATTED);
    }

    @Test
    void getEventsForDate_whenWeekStorageIsEmpty_returnEmptyString() {
        String result = baseExecutor.getEventsForDate(mockCommand, date, null);

        assertEquals("", result);
        checkUnsuccessfulCallMethods();
    }
}