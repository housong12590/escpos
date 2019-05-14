package com.cin.pos.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONUtil {

    private static Converter converter = findSupportJsonLibrary();

    public static List toList(String json) {
        checkNull();
        return converter.toBean(json, List.class);
    }

    public static Map toMap(String json) {
        checkNull();
        return converter.toBean(json, Map.class);
    }

    public static <T> T toBean(String json, Class<T> cls) {
        checkNull();
        return converter.toBean(json, cls);
    }

    public static String toJson(Object obj) {
        checkNull();
        return converter.toJson(obj);
    }

    private static void checkNull() {
        if (converter == null) {
            throw new NullPointerException("not found json parse library,You can use gson fastjson jackson");
        }
    }


    private static Converter findSupportJsonLibrary() {
        try {
            Class.forName("com.google.gson.Gson");
            return new GsonConverter();
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
            return new JacksonConverter();
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("com.alibaba.fastjson.JSON");
            return new FastjsonConverter();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }


    interface Converter {

        <T> T toBean(String json, Class<T> cls);

        String toJson(Object obj);
    }


    static class GsonConverter implements Converter {
        private Gson gson = new Gson();

        @Override
        public <T> T toBean(String json, Class<T> cls) {
            return gson.fromJson(json, cls);
        }

        @Override
        public String toJson(Object obj) {
            return gson.toJson(obj);
        }
    }

    static class FastjsonConverter implements Converter {

        @Override
        public <T> T toBean(String json, Class<T> cls) {
            return JSON.parseObject(json, cls);
        }

        @Override
        public String toJson(Object obj) {
            return JSON.toJSONString(obj);
        }
    }

    static class JacksonConverter implements Converter {
        private ObjectMapper mapper = new ObjectMapper();

        @Override
        public <T> T toBean(String json, Class<T> cls) {
            try {
                return mapper.readValue(json, cls);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toJson(Object obj) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
