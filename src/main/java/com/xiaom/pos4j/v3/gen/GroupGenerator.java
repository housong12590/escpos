package com.xiaom.pos4j.v3.gen;

import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.element.Group;
import com.xiaom.pos4j.v3.*;

import java.util.List;

public class GroupGenerator implements Generator<Group> {

    @Override
    public Group create(ElementExample example, Transform transform, Object env) {
        Group group = new Group();
        List<Property> properties = example.getProperties();
        if (properties.isEmpty()) {
            return null;
        }
        Object value = properties.get(0).getPlaceholders().get(0).getVariable().execute(transform, env);
        if (!(value instanceof List)) {
            return null;
        }
        List<?> list = (List<?>) value;
        ThisTransform thisTransform = new ThisTransform(transform);
        Dict newEnv = Dict.create(env);
        for (Object item : list) {
            newEnv.put(ThisTransform.THIS, item);
            for (ElementExample exampleChild : example.getChildren()) {
                generateChildElement(group, thisTransform, exampleChild, newEnv);
            }
        }
        return group;
    }

    private void generateChildElement(Group group, Transform transform, ElementExample example, Object env) {
        Class<?> elementClass = example.getElementClass();
        Class<? extends Generator> aClass = Template.gMAP.get(elementClass);
        try {
            Generator generator = aClass.newInstance();
            Element element = generator.create(example, transform, env);
            group.addChildren(element);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
