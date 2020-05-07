package com.ciin.pos.util;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    private static Converter converter = findSupportJsonLibrary();

    public static void setConverter(Converter converter) {
        JSONUtils.converter = converter;
    }

    public static <T> List<T> toList(String json, Class<T> cls) {
        checkNull();
        return converter.toList(json, cls);
    }

    public static <T> Map<String, T> toMap(String json, Class<T> cls) {
        checkNull();
        return converter.toMap(json, cls);
    }

    public static Object toBean(String json, Type type) {
        checkNull();
        return converter.toBean(json, type);
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
            throw new NullPointerException("not found json parse library,You can use gson fastjson jackson, add the appropriate dependency");
        }
    }


    private static Converter findSupportJsonLibrary() {
        try {
            Class.forName("com.google.gson.Gson");
            return new GsonConverter();
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("com.alibaba.fastjson.JSON");
            return new FastjsonConverter();
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
            return new JacksonConverter();
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }


    public interface Converter {

        <T> T toBean(String json, Class<T> cls);

        <T> List<T> toList(String json, Class<T> cls);

        <T> Map<String, T> toMap(String json, Class<T> cls);

        Object toBean(String json, Type type);

        String toJson(Object obj);
    }


    public static class GsonConverter implements Converter {
        public static final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.FINAL)
                .create();

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
            Type type = getType(Map.class, String.class, cls);
            return gson.fromJson(json, type);
        }

        @Override
        public Object toBean(String json, Type type) {
            return gson.fromJson(json, type);
        }

        @Override
        public String toJson(Object obj) {
            return gson.toJson(obj);
        }
    }

    public static class FastjsonConverter implements Converter {

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
            Type type = getType(Map.class, String.class, cls);
            return JSON.parseObject(json, type);
        }

        @Override
        public Object toBean(String json, Type type) {
            return JSON.parseObject(json, type);
        }

        @Override
        public String toJson(Object obj) {
            return JSON.toJSONString(obj);
        }
    }

    public static class JacksonConverter implements Converter {

        private ObjectMapper mapper;

        public JacksonConverter() {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

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
                JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, cls);
                return mapper.readValue(json, listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public <T> Map<String, T> toMap(String json, Class<T> cls) {
            try {
                JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, String.class, cls);
                return mapper.readValue(json, javaType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Object toBean(String json, Type type) {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            try {
                return mapper.readValue(json, javaType);
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

    private static Type getType(Class<?> raw, Class<?>... cls) {
        return new ParameterizedTypeImpl(raw, cls);
    }

    public static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        ParameterizedTypeImpl(Class raw, Type[] args) {
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
