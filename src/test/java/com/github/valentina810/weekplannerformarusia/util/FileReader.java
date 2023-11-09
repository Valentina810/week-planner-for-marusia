package com.github.valentina810.weekplannerformarusia.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

@Slf4j
public class FileReader {

    /**
     * Возвращает содержимое файла на входе в виде JsonObject - он должен быть корректным
     *
     * @param fileName - имя файла
     * @return - JsonObject, который удалось прочитать из файла
     */
    public static JsonObject loadJsonFromFile(String fileName) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(DataSourceFactory.class
                .getClassLoader()
                .getResourceAsStream(fileName)))) {
            return new JsonParser().parse(inputStreamReader).getAsJsonObject();
        } catch (Exception e) {
            log.info("Произошла ошибка {} при попытке открыть файл {}", e.getMessage(), fileName);
        }
        return new JsonObject();
    }

    /**
     * Возвращает содержимое файла на входе в виде строки
     *
     * @param fileName - имя файла
     * @return - строка с содержимым файла
     */
    public static String loadStringFromFile(String fileName) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(DataSourceFactory.class
                .getClassLoader()
                .getResourceAsStream(fileName)));
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            log.info("Произошла ошибка {} при попытке открыть файл {}", e.getMessage(), fileName);
        }
        return "";
    }
}