package com.xiaom.pos4j.comm;

public interface BasicTypeGetter<K> {

    <T> T getValue(String key, T defValue);

    String getString(K key, String defValue);

    Integer getInt(K key, Integer defValue);

    Float getFloat(K key, Float defValue);

    Double getDouble(K key, Double defValue);

    Long getLong(K key, Long defValue);

    Boolean getBool(K key, Boolean defValue);

    default <T> T getValue(String key) {
        return getValue(key, null);
    }

    default String getString(K key) {
        return getString(key, null);
    }

    default Integer getInt(K key) {
        return getInt(key, 0);
    }

    default Float getFloat(K key) {
        return getFloat(key, 0F);
    }

    default Double getDouble(K key) {
        return getDouble(key, 0D);
    }

    default Long getLong(K key) {
        return getLong(key, 0L);
    }

    default Boolean getBool(K key) {
        return getBool(key, false);
    }

}
