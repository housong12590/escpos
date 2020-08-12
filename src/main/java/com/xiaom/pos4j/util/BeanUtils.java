package com.xiaom.pos4j.util;


import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeanUtils {

    public static <T> T copyProperties(Object source, Class<T> cls) {
        try {
            T instance = cls.newInstance();
            copyProperties(source, instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void copyProperties(Object source, Object target) {
        Map<String, Field> sourceMap = getFieldMap(source);
        Map<String, Field> targetMap = getFieldMap(target);
        targetMap.forEach(new BiConsumer<String, Field>() {
            @Override
            public void accept(String key, Field targetField) {
                Field sourceField = sourceMap.get(key);
                try {
                    if (sourceField != null) {
                        Object value = sourceField.get(source);
                        if (value == null) {
                            return;
                        }
                        if (sourceField.getType() != targetField.getType()) {
                            value = ConvertUtils.matchTypeCast(targetField.getType(), value);
                        }
                        targetField.set(target, value);
                    }
                } catch (Exception ignored) {

                }
            }
        });
    }

    private static Map<String, Field> getFieldMap(Object obj) {
        Map<String, Field> maps = new HashMap<>();
        if (obj != null) {
            for (Field field : ClassUtils.getFields(obj)) {
                field.setAccessible(true);
                String fieldName = field.getName();
                maps.put(fieldName, field);
            }
        }
        return maps;
    }

    public static <Key, Entity> Map<Key, Entity> toMap(Collection<Entity> list, Function<Entity, Key> mapping) {
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(mapping, v -> v));
    }

    public static <E, R> List<R> toList(Collection<E> list, Function<E, R> mapping) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(mapping).collect(Collectors.toList());
    }

    public static <T> T toBean(Map<?, ?> map, Class<T> bean) {
        try {
            T object = bean.newInstance();
            for (Field field : ClassUtils.getFields(object)) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object value = map.get(field.getName());
                if (value instanceof Map) {
                    value = BeanUtils.toBean((Map<?, ?>) value, type);
                } else if (value instanceof List && type == List.class) {
                    Class<?> cls = ClassUtils.getParameterizedType(field);
                    List<Object> _list = new ArrayList<>();
                    ((List<?>) value).forEach(o -> _list.add(toBean((Map<?, ?>) o, cls)));
                    value = _list;
                } else {
                    value = ConvertUtils.matchTypeCast(type, value);
                }
                if (value != null) {
                    field.set(object, value);
                }
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T toBean2(Object value, Class<T> cls) {
        String json = JSONUtils.toJson(value);
        return JSONUtils.toBean(json, cls);
    }

    public static <T> T toBeanOrDefault(String json, Class<T> cls) {
        if (StringUtils.isEmpty(json)) {
            try {
                return cls.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return JSONUtils.toBean(json, cls);
    }

    public static List<Object> toListOrDefault(String json) {
        if (StringUtils.isEmpty(json)) {
            return new ArrayList<>();
        }
        return JSONUtils.toBean(json, List.class);
    }

    public static <T> List<T> toListOrDefault(String json, Class<T> cls) {
        return toListOrDefault(json, cls, new ArrayList<>());
    }

    public static <T> List<T> toListOrDefault(String json, Class<T> cls, List<T> defValue) {
        if (StringUtils.isEmpty(json)) {
            return defValue;
        }
        return JSONUtils.toList(json, cls);
    }

    public static Map<String, Object> toMap(Object bean) {
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : ClassUtils.getFields(bean)) {
                field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(bean);
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}