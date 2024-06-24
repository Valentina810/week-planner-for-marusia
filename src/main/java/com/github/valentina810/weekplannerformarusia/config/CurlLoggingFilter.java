package com.github.valentina810.weekplannerformarusia.config;


import com.github.valentina810.weekplannerformarusia.util.CachedBodyHttpServletRequest;
import com.github.valentina810.weekplannerformarusia.util.CurlCommandBuilder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class CurlLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Optional<CachedBodyHttpServletRequest> cachedBodyHttpServletRequest =
                getCachedBodyHttpServletRequest(httpServletRequest)
                        .or(() -> handleIOExceptionDuringCreation(request, response, chain));
        cachedBodyHttpServletRequest.ifPresentOrElse(
                cachedRequest -> {
                    log.info("------------------------------------------------------------------------------------------------------");
                    log.info("Получен входящий запрос");
                    log.info(CurlCommandBuilder.buildCurlCommand(cachedRequest));
                    handleFilterChain(response, chain, cachedRequest);
                },
                () -> log.warn("Не удалось получить CachedBodyHttpServletRequest для запроса")
        );
    }

    private Optional<CachedBodyHttpServletRequest> getCachedBodyHttpServletRequest(HttpServletRequest request) {
        try {
            return Optional.of(new CachedBodyHttpServletRequest(request));
        } catch (IOException e) {
            log.error("Ошибка при создании CachedBodyHttpServletRequest: ", e);
            return Optional.empty();
        }
    }

    private Optional<CachedBodyHttpServletRequest> handleIOExceptionDuringCreation(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            log.error("Ошибка при выполнении фильтрации запроса: ", e);
        }
        return Optional.empty();
    }

    private void handleFilterChain(ServletResponse response, FilterChain chain, CachedBodyHttpServletRequest cachedRequest) {
        try {
            chain.doFilter(cachedRequest, response);
        } catch (IOException | ServletException e) {
            log.error("Ошибка при выполнении фильтрации запроса: ", e);
            if (e instanceof IOException) {
                throw new RuntimeException(e);
            } else if (e instanceof ServletException) {
                throw new RuntimeException(e);
            }
        }
    }
}