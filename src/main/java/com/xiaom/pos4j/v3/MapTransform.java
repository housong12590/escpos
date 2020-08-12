package com.xiaom.pos4j.v3;

import java.util.Map;

public class MapTransform implements Transform<Map<String, Object>, Object> {

    private static MapTransform INSTANCE = new MapTransform();

    public static MapTransform get() {
        return INSTANCE;
    }

    @Override
    public Object get(String key, Map<String, Object> env) {
        return env.get(key);
    }
}
