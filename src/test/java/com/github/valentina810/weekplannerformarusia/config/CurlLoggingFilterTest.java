package com.github.valentina810.weekplannerformarusia.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.github.valentina810.Converter;
import com.github.valentina810.utils.Data;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static jakarta.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurlLoggingFilterTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @InjectMocks
    private CurlLoggingFilter filter;

    @Mock
    private Appender<ILoggingEvent> mockAppender;

    @BeforeAll
    void init()
    {
        MockitoAnnotations.openMocks(this);
        Logger rootLogger = (Logger) LoggerFactory.getLogger(CurlLoggingFilter.class);
        rootLogger.addAppender(mockAppender);
    }

    @BeforeEach
    void setUp() {
        filter = new CurlLoggingFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @Test
    @SneakyThrows
    void shouldLogRequestAndProcessFilterChainForValidPostRequest() {
        request.setMethod("POST");
        request.addHeader("User-Agent", "MailRu-VC/1.0");
        Converter converter = mock(Converter.class);
        Data data = mock(Data.class);
        when(converter.convert(any(HttpServletRequest.class))).thenReturn(data);
        when(data.getCurl()).thenReturn("curl command");

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(any(), any());
        assertEquals(SC_OK, response.getStatus());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeForInvalidUserAgent()  {
        request.setMethod("POST");
        request.addHeader("User-Agent", "Invalid-Agent");

        filter.doFilter(request, response, filterChain);

        assertEquals(SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
        verifyNoMoreInteractions(filterChain);
    }

    @Test
    void shouldReturnUnsupportedMediaTypeForEmptyUserAgent()  {
        request.setMethod("POST");

        filter.doFilter(request, response, filterChain);

        assertEquals(SC_UNSUPPORTED_MEDIA_TYPE, response.getStatus());
        verifyNoMoreInteractions(filterChain);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"})
    void shouldReturnMethodNotAllowedForNonPostRequests(String method) {
        request.setMethod(method);
        request.addHeader("User-Agent", "MailRu-VC/1.0");

        filter.doFilter(request, response, filterChain);

        assertEquals(SC_METHOD_NOT_ALLOWED, response.getStatus());
        verifyNoMoreInteractions(filterChain);
    }

    @Test
    @SneakyThrows
    void shouldHandleExceptionDuringRequestProcessing() {
        request.setMethod("POST");
        request.addHeader("User-Agent", "MailRu-VC/1.0");
        doThrow(new IOException("Test IOException")).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());
        ArgumentCaptor<ILoggingEvent> captor = ArgumentCaptor.forClass(ILoggingEvent.class);
        verify(mockAppender, atLeastOnce()).doAppend(captor.capture());
        assertTrue(captor.getAllValues().stream()
                .anyMatch(event -> event.getFormattedMessage().equals("Возникла ошибка при обработке запроса: Test IOException")));
    }
}