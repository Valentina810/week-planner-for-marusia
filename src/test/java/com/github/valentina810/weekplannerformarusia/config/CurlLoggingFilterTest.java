package com.github.valentina810.weekplannerformarusia.config;

import com.github.valentina810.Converter;
import com.github.valentina810.utils.Data;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static jakarta.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CurlLoggingFilterTest {

    private CurlLoggingFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

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

        doThrow(new IOException()).when(filterChain).doFilter(any(), any());

        filter.doFilter(request, response, filterChain);

        assertEquals(SC_OK, response.getStatus());
    }
}