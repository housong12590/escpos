package com.xiaom.pos4j.v3;

public interface Transform {

    <T> T get(String key, Object env);
}
