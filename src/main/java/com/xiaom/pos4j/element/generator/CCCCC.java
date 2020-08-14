package com.xiaom.pos4j.element.generator;

import com.xiaom.pos4j.parser.Property;
import com.xiaom.pos4j.parser.Transform;

public interface CCCCC<T> {

    void apply(T element, Property property, Transform transform, Object env);
}
