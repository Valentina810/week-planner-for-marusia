package com.github.valentina810.weekplannerformarusia.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;

import static java.lang.System.lineSeparator;
import static java.util.Collections.list;
import static java.util.stream.Collectors.joining;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CurlCommandBuilder {

    public static String buildCurlCommand(HttpServletRequest request) {
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

    private static String getCurlBody(HttpServletRequest request) {
        StringBuilder curlBody = new StringBuilder();
        String data = " --data '";
        try (BufferedReader reader = request.getReader()) {
            String body = reader.lines().collect(joining(lineSeparator()));
            if (!body.isEmpty()) {
                curlBody.append(data).append(body).append("'");
            }
        } catch (IOException e) {
            curlBody.append(data).append("Не удалось получить тело запроса").append("'");
        }
        return curlBody.toString();
    }

    private static String getHeaders(HttpServletRequest request) {
        return list(request.getHeaderNames())
                .stream()
                .flatMap(headerName -> list(request.getHeaders(headerName))
                        .stream()
                        .map(headerValue -> " -H '" + headerName + ": " + headerValue + "'"))
                .collect(joining(" "));
    }


    private static String getQueryParams(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return (queryString != null) ? "?" + queryString + "'" : "'";
    }
}