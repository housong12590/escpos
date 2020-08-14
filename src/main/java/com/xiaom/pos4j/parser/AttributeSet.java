package com.xiaom.pos4j.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class AttributeSet implements Iterable<Attribute> {

    private LinkedHashMap<String, Attribute> attributes;
    private AttributeSet parent;
    private List<AttributeSet> children;
    private String elementName;

    public AttributeSet(String elementName) {
        this.elementName = elementName;
        attributes = new LinkedHashMap<>();
        children = new ArrayList<>();
    }

    public String get(String key) {
        Attribute attribute = attributes.get(key);
        if (attribute == null) {
            return "";
        }
        return attribute.getValue();
    }

    public void put(String key, String value) {
        Attribute attr = new Attribute(key, value);
        this.put(attr);
    }

    public void put(Attribute attribute) {
        attributes.put(attribute.getKey(), attribute);
    }

    public void remove(String key) {
        attributes.remove(key);
    }

    public boolean hasKey(String key) {
        return attributes.containsKey(key);
    }

    public int size() {
        return attributes.size();
    }

    public void addAll(AttributeSet attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        this.attributes.putAll(attributes.attributes);
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public List<Attribute> asList() {
        return new ArrayList<>(attributes.values());
    }

    public AttributeSet getParent() {
        return parent;
    }

    public void setParent(AttributeSet parent) {
        this.parent = parent;
    }

    public List<AttributeSet> getChildren() {
        return children;
    }

    public void addChildren(AttributeSet attributeSet) {
        this.children.add(attributeSet);
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public Iterator<Attribute> iterator() {
        return attributes.values().iterator();
    }
}
