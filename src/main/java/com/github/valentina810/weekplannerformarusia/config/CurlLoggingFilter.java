package com.github.valentina810.weekplannerformarusia.config;

import com.github.valentina810.weekplannerformarusia.util.CachedBodyHttpServletRequest;
import com.github.valentina810.weekplannerformarusia.util.CurlCommandBuilder;
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

import static jakarta.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;

@Slf4j
public class CurlLoggingFilter implements Filter {

    private static final String REQUIRED_USER_AGENT = "MailRu-VC/1.0";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String SUPPORTED_METHOD = "POST";
    private static final Consumer<String> LOG_ERROR = e -> log.error("Возникла ошибка при обработке запроса: {}", e);
    private static final BiConsumer<HttpServletRequest, HttpServletResponse> processRequest =
            (request, response) -> {
                if (SUPPORTED_METHOD.equalsIgnoreCase(request.getMethod())) {
                    if (!REQUIRED_USER_AGENT.equals(request.getHeader(USER_AGENT_HEADER))) {
                        try {
                            response.sendError(SC_UNSUPPORTED_MEDIA_TYPE);
                        } catch (IOException e) {
                            LOG_ERROR.accept(e.getMessage());
                        }
                    }
                }
            };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        if (servletRequest instanceof HttpServletRequest request && servletResponse instanceof HttpServletResponse response) {

            Optional<CachedBodyHttpServletRequest> cachedBodyHttpServletRequest =
                    getCachedBodyHttpServletRequest(request)
                            .or(Optional::empty);

            cachedBodyHttpServletRequest.ifPresentOrElse(
                    cachedRequest -> log.info(CurlCommandBuilder.buildCurlCommand(cachedRequest)),
                    Optional::empty);

            processRequest.accept(request, response);
            if (!response.isCommitted()) {
                try {
                    filterChain.doFilter(cachedBodyHttpServletRequest.get(), response);
                } catch (IOException | ServletException e) {
                    LOG_ERROR.accept(e.getMessage());
                }
            }
        }
    }

    private Optional<CachedBodyHttpServletRequest> getCachedBodyHttpServletRequest(HttpServletRequest request) {
        try {
            return Optional.of(new CachedBodyHttpServletRequest(request));
        } catch (IOException e) {
            log.error("Ошибка при создании CachedBodyHttpServletRequest: ", e);
            return Optional.empty();
        }
    }
}