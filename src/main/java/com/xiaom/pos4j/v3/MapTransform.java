package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.util.ClassUtils;

import java.util.Map;

public class MapTransform implements Transform {

    private static MapTransform INSTANCE = new MapTransform();

    public static MapTransform get() {
        return INSTANCE;
    }

    @Override
    public <T> T get(String key, Object env) {
        if (env instanceof Map) {
            Object value = ((Map<?, ?>) env).get(key);
            return ClassUtils.cast(value);
        }
        return null;
    }
}
