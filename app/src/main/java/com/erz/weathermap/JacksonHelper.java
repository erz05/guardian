package com.erz.weathermap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;

/**
 * Created by edgarramirez on 12/21/14.
 */
public class JacksonHelper {
    private static ObjectMapper getJSONInstance() {
        ObjectMapper jsi = new ObjectMapper();
        jsi.disable(MapperFeature.AUTO_DETECT_CREATORS);
        jsi.disable(MapperFeature.AUTO_DETECT_GETTERS);
        jsi.disable(MapperFeature.AUTO_DETECT_SETTERS);
        jsi.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        jsi.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        jsi.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return jsi;
    }

    public static TypeFactory getTypeFactory(){
        return getJSONInstance().getTypeFactory();
    }

    public static String writeValueAsJSON(Object object) {
        try {
            return getJSONInstance().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "ERR";
        }
    }

    public static Object readValue(String input, Class<?> type) {
        try {
            return getJSONInstance().readValue(input, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object readValue(String input, CollectionType type) {
        try {
            return getJSONInstance().readValue(input, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}