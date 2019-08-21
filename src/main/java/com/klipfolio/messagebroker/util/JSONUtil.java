package com.klipfolio.messagebroker.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public final class JSONUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JSONUtil() {}

    /**
     * If requireFields is true, than throws an error if any fields in the json string are missing.
     * Otherwise missing fields will be set to null in class.
     */
    public static Object parse(String jsonString, Class classType) throws IOException {
        return mapper.readValue(jsonString, classType);
    }

    public static Map parse(String jsonString) throws IOException {
        return mapper.readValue(jsonString, Map.class);
    }

    /**
     * Only getter methods will be converted to fields in the json string
     */
    public static String stringify(Object jsonObject) throws IOException {
        return mapper.writeValueAsString(jsonObject);
    }
}
