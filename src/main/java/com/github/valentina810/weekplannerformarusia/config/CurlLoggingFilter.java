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
import java.util.function.Predicate;

import static jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static java.util.Optional.of;

@Slf4j
public class CurlLoggingFilter implements Filter {

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String ALLOWED_USER_AGENT = "MailRu-VC/1.0";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        log.info(CurlCommandBuilder.buildCurlCommandRequest(new ContentCachingRequestWrapper(httpRequest)));

        Predicate<HttpServletRequest> isPostMethod = req -> "POST".equalsIgnoreCase(req.getMethod());
        Predicate<HttpServletRequest> hasAllowedUserAgent = req -> ALLOWED_USER_AGENT.equals(req.getHeader(USER_AGENT_HEADER));

        of(httpRequest)
                .filter(isPostMethod.and(hasAllowedUserAgent))
                .ifPresentOrElse(req ->
                                processSuccessfully(filterChain, req, httpResponse),
                        () -> returnErrorMethodNotSupported(httpResponse));
    }

    private static void returnErrorMethodNotSupported(HttpServletResponse httpResponse) {
        try {
            httpResponse.sendError(SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        } catch (IOException e) {
            log.error("Возникла ошибка при отправке сообщения о неподдерживаемом методе: ", e);
        }
    }

    private void processSuccessfully(FilterChain filterChain, HttpServletRequest req, HttpServletResponse httpResponse) {
        try {
            ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(req);
            ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(httpResponse);
            filterChain.doFilter(cachedRequest, cachedResponse);
            log.info(CurlCommandBuilder.buildCurlCommandResponse(cachedResponse));
            cachedResponse.copyBodyToResponse();
        } catch (IOException | ServletException e) {
            log.error("Возникла ошибка при логировании curl запроса/ответа: ", e);
        }
    }
}