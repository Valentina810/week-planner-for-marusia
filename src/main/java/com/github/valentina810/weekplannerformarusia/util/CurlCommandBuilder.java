package com.github.valentina810.weekplannerformarusia.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

import static java.util.Collections.list;
import static java.util.stream.Collectors.joining;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CurlCommandBuilder {

    public static String buildCurlCommandRequest(ContentCachingRequestWrapper request) {
        return new StringBuilder()
                .append("curl -X ")
                .append(request.getMethod())
                .append(" '")
                .append(request.getRequestURL())
                .append(getQueryParams(request))
                .append(getHeaders(request))
                .append(getCurlBody(request))
                .append(" # IP: ")
                .append(request.getRemoteAddr())
                .toString();
    }

    public static String buildCurlCommandResponse(ContentCachingResponseWrapper response) {
        return new StringBuilder()
                .append("curl -X ")
                .append(response.getStatus())
                .append(" '")
                .append(getHeaders(response)).append(" --data '")
                .append(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8))
                .toString();
    }

    private static String getCurlBody(ContentCachingRequestWrapper request) {
        return " --data '" + new String(request.getContentAsByteArray(), StandardCharsets.UTF_8) + "'";
    }

    private static String getHeaders(HttpServletRequest request) {
        return list(request.getHeaderNames())
                .stream()
                .flatMap(headerName -> list(request.getHeaders(headerName))
                        .stream()
                        .map(headerValue -> " -H '" + headerName + ": " + headerValue + "'"))
                .collect(joining(" "));
    }

    private static String getHeaders(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .map(headerName -> response.getHeaders(headerName).stream()
                        .map(headerValue -> String.format(" -H '%s: %s'", headerName, headerValue))
                        .collect(joining(" ")))
                .collect(joining(" "));
    }

    private static String getQueryParams(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return (queryString != null) ? "?" + queryString + "'" : "'";
    }
}