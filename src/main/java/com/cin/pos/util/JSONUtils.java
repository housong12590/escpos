package com.cin.pos.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    private static Converter converter = findSupportJsonLibrary();

    public static <T> List<T> toList(String json, Class<T> cls) {
        checkNull();
        return converter.toList(json, cls);
    }

    public static <T> Map<String, T> toMap(String json, Class<T> cls) {
        checkNull();
        return converter.toMap(json, cls);
    }

    public static Map toMap(Object obj) {
        checkNull();
        String json = converter.toJson(obj);
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

//        try {
//            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
//            return new JacksonConverter();
//        } catch (ClassNotFoundException ignored) {
//        }
//        try {
//            Class.forName("com.alibaba.fastjson.JSON");
//            return new FastjsonConverter();
//        } catch (ClassNotFoundException ignored) {
//        }
//
        try {
            Class.forName("com.google.gson.Gson");
            return new GsonConverter();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }


    interface Converter {

        <T> T toBean(String json, Class<T> cls);

        <T> List<T> toList(String json, Class<T> cls);

        <T> Map<String, T> toMap(String json, Class<T> cls);

        String toJson(Object obj);
    }


    static class GsonConverter implements Converter {
        private Gson gson = new Gson();

        @Override
        public <T> T toBean(String json, Class<T> cls) {
            return gson.fromJson(json, cls);
        }

        @Override
        public <T> List<T> toList(String json, Class<T> cls) {
            Type type = getType(List.class, cls);
            return gson.fromJson(json, type);
        }

        @Override
        public <T> Map<String, T> toMap(String json, Class<T> cls) {
            ParameterizedTypeImpl type = new ParameterizedTypeImpl(Map.class, new Class[]{cls});
            return gson.fromJson(json, type);
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
        public <T> List<T> toList(String json, Class<T> cls) {
            return JSON.parseArray(json, cls);
        }

        @Override
        public <T> Map<String, T> toMap(String json, Class<T> cls) {
            ParameterizedTypeImpl type = new ParameterizedTypeImpl(Map.class, new Class[]{cls});
            return JSON.parseObject(json, type);
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
        public <T> List<T> toList(String json, Class<T> cls) {
            try {
                return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<List<T>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public <T> Map<String, T> toMap(String json, Class<T> cls) {
            try {
                return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<Map<String, T>>() {
                });
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

    public static Type getType(Class<?> raw, Class<?>... cls) {
        return new ParameterizedTypeImpl(raw, cls);
    }

    public static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
