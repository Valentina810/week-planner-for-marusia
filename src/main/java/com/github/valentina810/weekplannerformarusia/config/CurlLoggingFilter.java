package com.github.valentina810.weekplannerformarusia.config;


import com.github.valentina810.Converter;
import com.github.valentina810.utils.Data;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static jakarta.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;

@Slf4j
public class CurlLoggingFilter implements Filter {

    private static final String REQUIRED_USER_AGENT = "MailRu-VC/1.0";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String SUPPORTED_METHOD = "POST";
    private static final Consumer<String> LOG_ERROR = e -> log.error("Возникла ошибка при обработке запроса: {}", e);
    private static final BiConsumer<HttpServletRequest, HttpServletResponse> processRequest = (request, response) -> {
        int responseCode = determineResponseCode(request);

        Optional.of(responseCode)
                .filter(code -> code != 0)
                .ifPresent(code -> sendErrorResponse(response, code));
    };

    private static int determineResponseCode(HttpServletRequest request) {
        boolean isUserAgentValid = REQUIRED_USER_AGENT.equals(request.getHeader(USER_AGENT_HEADER));
        boolean isMethodSupported = SUPPORTED_METHOD.equalsIgnoreCase(request.getMethod());

        return !isUserAgentValid ? SC_UNSUPPORTED_MEDIA_TYPE :
                !isMethodSupported ? SC_METHOD_NOT_ALLOWED : 0;
    }

    private static void sendErrorResponse(HttpServletResponse response, int responseCode) {
        try {
            response.sendError(responseCode);
        } catch (IOException e) {
            LOG_ERROR.accept(e.getMessage());
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        if (servletRequest instanceof HttpServletRequest request && servletResponse instanceof HttpServletResponse response) {
            Data convert = new Converter().convert(request);
            log.info("Входящий запрос {}", convert.getCurl());
            processRequest.accept(request, response);
            if (!response.isCommitted()) {
                try {
                    filterChain.doFilter(convert.getCachedBodyHttpServletRequest(), response);
                } catch (IOException | ServletException e) {
                    LOG_ERROR.accept(e.getMessage());
                }
            } else log.info("Запрос не был обработан по причине отсутствия заголовка User-Agent с требуемым значением или неподдерживаемого метода");
        }
    }
}