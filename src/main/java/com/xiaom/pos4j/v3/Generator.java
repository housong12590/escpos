package com.xiaom.pos4j.v3;

public interface Generator<T> {

    T create(ElementSample sample, Transform transform, Object env);
}
