package com.github.valentina810.weekplannerformarusia;


import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStreamReader;
import java.util.Objects;


@Slf4j
public class FileReader {

    /**
     * Возвращает содержимое файла на входе в виде JsonArray - он должен быть корректным
     *
     * @param fileName - имя файла
     * @return - JsonArray, который удалось прочитать из файла
     */
    public static JsonArray loadJsonFromFile(String fileName) {

        try (InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(FileReader.class.getClassLoader().getResourceAsStream(fileName)))) {
            return new JsonParser().parse(inputStreamReader).getAsJsonArray();
        } catch (Exception e) {
            log.info("Произошла ошибка {} при попытке открыть файл {}", e.getMessage(), fileName);
        }
        return new JsonArray();
    }
}