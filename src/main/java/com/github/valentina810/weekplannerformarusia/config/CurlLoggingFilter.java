package com.github.valentina810.weekplannerformarusia.config;


import com.github.valentina810.weekplannerformarusia.util.CurlCommandBuilder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
public class CurlLoggingFilter implements Filter {

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String ALLOWED_USER_AGENT = "MailRu-VC/1.0";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        Predicate<HttpServletRequest> isPostMethod = req -> "POST".equalsIgnoreCase(req.getMethod());
        Predicate<HttpServletRequest> hasAllowedUserAgent = req -> ALLOWED_USER_AGENT.equals(req.getHeader(USER_AGENT_HEADER));

        Optional<HttpServletRequest> optionalRequest = Optional.of(httpRequest)
                .filter(isPostMethod.and(hasAllowedUserAgent));

        log.info("------------------------------------------------------------------------------------------------------");
        optionalRequest.ifPresentOrElse(
                req -> processSuccessfully(filterChain, req, httpResponse),
                () -> returnErrorMethodNotSupported(httpResponse, httpRequest)
        );
        log.info("------------------------------------------------------------------------------------------------------");
    }

    private void processSuccessfully(FilterChain filterChain, HttpServletRequest req, HttpServletResponse httpResponse) {
        try {
            ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(req);
            ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(httpResponse);
            filterChain.doFilter(cachedRequest, cachedResponse);
            logRequestResponseAsCurl(cachedRequest, cachedResponse);
            cachedResponse.copyBodyToResponse();
        } catch (IOException | ServletException e) {
            log.error("Возникла ошибка при логировании curl запроса/ответа: ", e);
        }
    }

    private static void returnErrorMethodNotSupported(HttpServletResponse httpResponse, HttpServletRequest httpRequest) {
        try {
            httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
            log.info(CurlCommandBuilder.buildCurlCommandRequest(new ContentCachingRequestWrapper(httpRequest)));
            log.info("405 Method Not Allowed");
        } catch (IOException e) {
            log.error("Возникла ошибка при отправке сообщения о неподдерживаемом методе: ", e);
        }
    }

    private void logRequestResponseAsCurl(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        log.info(CurlCommandBuilder.buildCurlCommandRequest(request));
        log.info(CurlCommandBuilder.buildCurlCommandResponse(response));
    }
}