package com.xiaom.pos4j.v3;

import java.util.Map;

public class ElementSample {

    private Class<?> elementClass;
    private Map<String, Properties> elementProperty;

    public Class<?> getElementClass() {
        return elementClass;
    }

    public void setElementClass(Class<?> elementClass) {
        this.elementClass = elementClass;
    }

    public Map<String, Properties> getElementProperty() {
        return elementProperty;
    }

    public void setElementProperty(Map<String, Properties> elementProperty) {
        this.elementProperty = elementProperty;
    }
}
