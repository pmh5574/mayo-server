package com.mayo.server.common.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommonUtility {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static Long getId(Object id) {

        return Long.parseLong(id.toString());
    }

    public static <T> T getOrDefaultEmpty(T value, T defaultValue) {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public static  <T> T getMappingJson(
            JSONObject json,
            Class<T> recordClass) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false
        );

        try {
            return objectMapper.readValue(json.toString(), recordClass);  // JSONObject의 toJSONString 대신 toString 사용
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJSONString(Object value) {

        try {

            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
