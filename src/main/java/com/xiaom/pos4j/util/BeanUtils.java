package com.xiaom.pos4j.util;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {


    public static <T> T copyProperties(Object source, Class<T> cls) {
        try {
            T target = cls.newInstance();
            copyProperties(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copyProperties(Object source, Object target) {
        copyProperties0(source, target);
    }

    public static void copyProperties0(Object source, Object target) {
        if (source == null || target == null) return;
        Class<?> srcClass = source.getClass();
        Class<?> dstClass = target.getClass();
        for (Field srcField : srcClass.getDeclaredFields()) {
            if (ClassUtils.isStatic(srcField)) continue;
            if (!ClassUtils.isPublic(srcField)) {
                srcField.setAccessible(true);
            }
            try {
                Field dstField = dstClass.getDeclaredField(srcField.getName());
                if (dstField == null) continue;
                if (!ClassUtils.isPublic(dstField)) {
                    dstField.setAccessible(true);
                }
                Class<?> srcType = srcField.getType();
                Class<?> dstType = dstField.getType();
                Object value = srcField.get(source);
                if (value == null) continue;
                if (srcType != dstType) {
                    value = ConvertUtils.matchTypeCast(srcType, value);
                }
                dstField.set(target, value);
            } catch (Exception ignored) {

            }
        }
    }

    public static <T> T mapToBean(Map<?, ?> map, Class<T> cls) {
        if (map == null) return null;
        T obj;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
        for (Field f : cls.getDeclaredFields()) {
            if (ClassUtils.isStatic(f)) continue;
            if (!ClassUtils.isPublic(f)) f.setAccessible(true);
            Object value = map.get(f.getName());
            if (value == null) continue;
            Class<?> fieldType = f.getType();
            try {
                if (fieldType.isInstance(value)) {
                    f.set(obj, value);
                } else if (ClassUtils.isPrimitive(fieldType)) {
                    value = ConvertUtils.matchTypeCast(cls, value);
                    f.set(obj, value);
                }
            } catch (Exception ignored) {

            }
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(Object value, Class<T> cls) {
        if (value == null) return null;
        else if (cls.isInstance(value)) return (T) value;
        else if (value instanceof Map) {
            return mapToBean((Map<?, ?>) value, cls);
        } else {
            return copyProperties(value, cls);
        }
    }

    public static <T> T toBean2(Object value, Class<T> cls) {
        String json = JSONUtils.toJson(value);
        return JSONUtils.toBean(json, cls);
    }

    public static Map<String, Object> toMap(Object bean) {
        Map<String, Object> map = new HashMap<>();
        toMap(bean, map);
        return map;
    }

    public static void toMap(Object bean, Map<String, Object> map) {
        try {
            for (Field field : ClassUtils.getFields(bean)) {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(bean);
                map.put(key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}