package com.github.valentina810.weekplannerformarusia.util;


import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;


@Slf4j
@UtilityClass
public class JsonFileReader {

    /**
     * Возвращает содержимое файла на входе в виде JsonArray - он должен быть корректным
     *
     * @param fileName - имя файла
     * @return - JsonArray, который удалось прочитать из файла
     */
    @SneakyThrows
    public static JsonArray readJsonArrayFromFile(String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String jsonContent = reader.lines().collect(Collectors.joining());
            return JsonParser.parseString(jsonContent).getAsJsonArray();
        }
    }
}