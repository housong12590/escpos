package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.element.*;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.v3.Transform;

import java.util.HashMap;
import java.util.Map;

public class GeneratorFactory {

    public static Map<Class<? extends Element>, Generator<? extends Element>> elementGenerator = new HashMap<>();

    static {
        elementGenerator.put(Text.class, new TextGenerator());
        elementGenerator.put(Image.class, new ImageGenerator());
        elementGenerator.put(Section.class, new SectionGenerator());
        elementGenerator.put(Table.class, new TableGenerator());
        elementGenerator.put(Group.class, new GroupGenerator());
    }

    public static Element getElement(ElementExample example, Transform transform, Object env) {
        Class<? extends Element> elementClass = example.getElementClass();
        Generator<? extends Element> generator = elementGenerator.get(elementClass);
        return generator.create(example, transform, env);
    }

}
