package com.xiaom.pos4j.v3;

import java.util.List;

public class ElementExample {

    private Class<?> elementClass;
    private ElementExample[] children;
    private List<Property> properties;

    public Class<?> getElementClass() {
        return elementClass;
    }

    public void setElementClass(Class<?> elementClass) {
        this.elementClass = elementClass;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
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
