package com.hstmpl.escpos.util;


import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> Map<K, V> of(K key, V value) {
        Map<K, V> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }


    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> filter(Map<K, V> map, K... keys) {
        if (isEmpty(map)) return new HashMap<>();
        Map<K, V> result = new HashMap<>(map);
        for (K key : keys) {
            result.remove(key);
        }
        return result;
    }

    public static void clear(Map<?, ?>... maps) {
        for (Map<?, ?> map : maps) {
            if (!isEmpty(map)) map.clear();
        }
    }

    public static <K, V> V get(Map<?, ?> map, K key) {
        return get(map, key, null);
    }

    public static <K, V> V get(Map<?, ?> map, K key, V defValue) {
        Object value = map.get(key);
        if (value == null) {
            return defValue;
        }
        return ClassUtils.cast(value);
    }

    public static <K> Long getLong(Map<?, ?> map, K key) {
        return getLong(map, key, null);
    }

    public static <K> Long getLong(Map<?, ?> map, K key, Long defValue) {
        return ConvertUtils.toLong(map.get(key), defValue);
    }

    public static <K> String getString(Map<?, ?> map, K key) {
        return getString(map, key, null);
    }

    public static <K> String getString(Map<?, ?> map, K key, String defValue) {
        return ConvertUtils.toString(map.get(key), defValue);
    }

    public static <K> Integer getInt(Map<?, ?> map, K key) {
        return getInt(map, key, null);
    }

    public static <K> Integer getInt(Map<?, ?> map, K key, Integer defValue) {
        Object value = map.get(key);
        if (value == null) return defValue;
        if (value instanceof Integer) return (Integer) value;
        return ConvertUtils.toDouble(value).intValue();
    }

    public static <K> Boolean getBool(Map<?, ?> map, K key) {
        return getBool(map, key, false);
    }

    public static <K> Boolean getBool(Map<?, ?> map, K key, Boolean defValue) {
        Object value = map.get(key);
        if (value == null) return defValue;
        return ConvertUtils.toBool(value, defValue);
    }

    public static <K> Double getDouble(Map<?, ?> map, K key) {
        return getDouble(map, key, null);
    }

    public static <K> Double getDouble(Map<?, ?> map, K key, Double defValue) {
        return ConvertUtils.toDouble(map.get(key), defValue);
    }

    public static <K> Float getFloat(Map<?, ?> map, K key) {
        return getFloat(map, key, null);
    }

    public static <K> Float getFloat(Map<?, ?> map, K key, Float defValue) {
        return ConvertUtils.toFloat(map.get(key), defValue);
    }


    @SuppressWarnings("unchecked")
    public static <K> Map<String, Object> getMap(Map<?, ?> map, K key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return BeanUtils.toMap(value);
    }


    public static <T> T getValue(Map<?, ?> map, String key) {
        return getValue(map, key, null);
    }

    public static <T> T getValue(Map<?, ?> map, String key, T defValue) {
        T value = Expression.of(key).getValue(map);
        if (value == null) {
            return defValue;
        }
        return value;
    }

}
