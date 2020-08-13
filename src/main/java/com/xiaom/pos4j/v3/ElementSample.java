package com.xiaom.pos4j.v3;

import java.util.List;

public class ElementSample {

    private Class<?> elementClass;
    private ElementSample parentElement;
    private List<Property> properties;

    public Class<?> getElementClass() {
        return elementClass;
    }

    public void setElementClass(Class<?> elementClass) {
        this.elementClass = elementClass;
    }

    public ElementSample getParentElement() {
        return parentElement;
    }

    public void setParentElement(ElementSample parentElement) {
        this.parentElement = parentElement;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ElementSample{" +
                "elementClass=" + elementClass +
                ", parentElement=" + parentElement +
                ", elementProperty=" + properties +
                '}';
    }
}
