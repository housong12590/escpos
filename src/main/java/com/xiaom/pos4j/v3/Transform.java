package com.xiaom.pos4j.v3;

public interface Transform<T, R> {

    R get(String key, T env);
}
