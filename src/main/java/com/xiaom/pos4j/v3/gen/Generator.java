package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.v3.Transform;

public interface Generator<T extends Element> {

    T create(ElementExample example, Transform transform, Object env);

}
