package com.hstmpl.escpos.parser;

import com.hstmpl.escpos.element.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementExample {

    private Class<? extends Element> elementClass;
    private ElementExample[] children;
    private Map<String, Property> properties;

    public ElementExample() {
        properties = new HashMap<>();
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

    @Override
    public String toString() {
        return elementClass.toString();
    }
}
