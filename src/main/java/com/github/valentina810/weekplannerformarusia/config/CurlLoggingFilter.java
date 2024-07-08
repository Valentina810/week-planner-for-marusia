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

@Slf4j
public class CurlLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(cachedRequest, cachedResponse);
        logRequestResponseAsCurl(cachedRequest, cachedResponse);
        cachedResponse.copyBodyToResponse();
    }

    private void logRequestResponseAsCurl(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        log.info(CurlCommandBuilder.buildCurlCommandRequest(request));
        log.info(CurlCommandBuilder.buildCurlCommandResponse(response));
    }
}