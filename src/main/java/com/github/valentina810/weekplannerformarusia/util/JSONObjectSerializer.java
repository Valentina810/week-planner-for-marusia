package com.github.valentina810.weekplannerformarusia.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.io.IOException;

@Slf4j
public class JSONObjectSerializer extends JsonSerializer<JSONObject> {

    @Override
    public void serialize(JSONObject value, JsonGenerator gen, SerializerProvider serializers) {
        try {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeRawValue(value.toString());
            }
        } catch (IOException e) {
            log.info("Возникла ошибка при сериализации объекта {}", e.getMessage());
        }
    }
}