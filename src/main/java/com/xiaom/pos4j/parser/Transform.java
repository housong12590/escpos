package com.xiaom.pos4j.parser;

public interface Transform {

    <T> T get(String key, Object env);
}
