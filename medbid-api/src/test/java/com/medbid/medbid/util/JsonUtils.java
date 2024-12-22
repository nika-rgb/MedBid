package com.medbid.medbid.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private JsonUtils() {
    }

    public static final <T> T convertJsonString(String json, Class<T> clazz, ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }

}
