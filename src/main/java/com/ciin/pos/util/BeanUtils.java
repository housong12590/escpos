package com.ciin.pos.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Field[] targetFields = target.getClass().getDeclaredFields();
        for (Field targetField : targetFields) {
            targetField.setAccessible(true);
            try {
                Field sourceField = source.getClass().getDeclaredField(targetField.getName());
                if (targetField.getType() == sourceField.getType()) {
                    sourceField.setAccessible(true);
                }
                targetField.set(target, sourceField.get(source));
            } catch (Exception ignored) {

            }
        }
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


    public static <T> T toBean(Map<String, ?> map, Class<T> bean) {
        try {
            T object = bean.newInstance();
            BeanInfo beaninfo = Introspector.getBeanInfo(bean, Object.class);
            PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
            for (PropertyDescriptor property : pro) {
                String name = property.getName();
                Object value = map.get(name);
                Method set = property.getWriteMethod();
                set.invoke(object, value);
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Map<String, Object> toMap(Object bean) {
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
            PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
            for (PropertyDescriptor property : pro) {
                String key = property.getName();
                Method get = property.getReadMethod();
                Object value = get.invoke(bean);
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}