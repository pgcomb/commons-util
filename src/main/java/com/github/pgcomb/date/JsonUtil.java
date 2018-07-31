package com.github.pgcomb.date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import java.io.IOException;
import java.util.List;

/**
 * Title: JsonUtil <br>
 * Description: JsonUtil <br>
 * Date: 2018年07月30日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class JsonUtil {

    public static ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new AfterburnerModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);
        return objectMapper;
    }

    public static <T> T str2bean(String json) throws IOException {
        return str2bean(json, new TypeReference<T>() {
        });
    }

    public static <T> List<T> str2List(String json) throws IOException {
        return str2bean(json, new TypeReference<List<T>>() {
        });
    }

    public static <T> T str2bean(String json, TypeReference typeReference) throws IOException {
        return objectMapper().readValue(json, typeReference);
    }

    public static String bean2str(Object object) throws JsonProcessingException {
        return objectMapper().writeValueAsString(object);
    }
}
