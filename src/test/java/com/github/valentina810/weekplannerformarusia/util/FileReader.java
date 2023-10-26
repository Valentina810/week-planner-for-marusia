package com.github.valentina810.weekplannerformarusia.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

import java.io.InputStreamReader;
import java.util.Objects;

@Slf4j
public class FileReader {
    public static JsonObject loadJsonFromFile(String fileName) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(DataSourceFactory.class
                .getClassLoader()
                .getResourceAsStream(fileName)))) {
            return new JsonParser().parse(inputStreamReader).getAsJsonObject();
        } catch (Exception e) {
            log.info("Произошла ошибка {} при попытке открыть файл {}", e.getMessage(), fileName);
        }
        return null;
    }
}