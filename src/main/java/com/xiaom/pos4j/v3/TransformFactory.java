package com.xiaom.pos4j.v3;

import java.util.HashMap;
import java.util.Map;

public class TransformFactory {

    private Map<Class<?>, Transform> MAP = new HashMap<>();

    public void register(Class<?> cls, Transform transform) {
        MAP.put(cls, transform);
    }

    public Transform get(Class<?> cls) {
        return MAP.get(cls);
    }

    public static void main(String[] args) {
        TransformFactory factory = new TransformFactory();
        Transform transform = factory.get(Map.class);
//        Object o = transform.get("");
    }
}
