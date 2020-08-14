package com.xiaom.pos4j.parser;

import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.element.generator.CCCCC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementExample {

    private Class<? extends Element> elementClass;
    private ElementExample[] children;
    private Map<String, Property> properties;
    private Element element;
    private List<CCCCC<Element>> mappings;

    public ElementExample() {
        properties = new HashMap<>();
        mappings = new ArrayList<>();
    }

    public Class<? extends Element> getElementClass() {
        return elementClass;
    }

    public void setElementClass(Class<? extends Element> elementClass) {
        this.elementClass = elementClass;
    }

    public List<Property> getProperties() {
        return new ArrayList<>(properties.values());
    }

    public Property getProperty(String name) {
        return properties.get(name);
    }

    public void addProperty(Property property) {
        properties.put(property.getName(), property);
    }

    public ElementExample[] getChildren() {
        return children;
    }

    public void setChildren(ElementExample[] children) {
        this.children = children;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public List<CCCCC<Element>> getMappings() {
        return mappings;
    }

    public void addMapping(CCCCC<Element> e) {
        mappings.add(e);
    }

    @Override
    public String toString() {
        return elementClass.toString();
    }
}
