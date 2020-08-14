package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.element.Group;
import com.xiaom.pos4j.parser.ElementExample;
import com.xiaom.pos4j.parser.Placeholder;
import com.xiaom.pos4j.parser.Property;
import com.xiaom.pos4j.v3.MapTransform;
import com.xiaom.pos4j.v3.Transform;

import java.util.List;

public class GroupGenerator implements Generator<Group> {

    @Override
    public Group create(ElementExample example, Transform transform, Object env) {
        Group group = new Group();
        List<Property> properties = example.getProperties();
        if (properties.isEmpty()) {
            return null;
        }
        Property itemProperty = example.getProperty("item");
        Property listProperty = example.getProperty("list");
        List<Placeholder> placeholders = listProperty.getPlaceholders();
        if (placeholders == null || placeholders.isEmpty()) {
            return null;
        }
        Object value = placeholders.get(0).getVariable().execute(transform, env);
        if (!(value instanceof List)) {
            return null;
        }
        List<?> list = (List<?>) value;
        Dict newEnv = Dict.create(env);
        for (Object item : list) {
            newEnv.put(itemProperty.getValue(), item);
            for (ElementExample exampleChild : example.getChildren()) {
                Element element = GeneratorFactory.getElement(exampleChild, MapTransform.get(), newEnv);
                if (element != null) {
                    group.addChildren(element);
                }
            }
        }
        return group;
    }
}
