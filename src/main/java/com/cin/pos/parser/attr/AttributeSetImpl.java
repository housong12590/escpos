package com.cin.pos.parser.attr;

import com.cin.pos.util.Util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AttributeSetImpl implements AttributeSet {

    private Map<String, Object> attributeSet = new HashMap<>();
    private LinkedList<AttributeSet> attributeSets = new LinkedList<>();
    private AttributeSetImpl parentAttr;
    private String name;

    public AttributeSetImpl(String name) {
        this.name = name;
    }

    public void put(String attribute, Object value) {
        attributeSet.put(attribute, value);
    }

    public AttributeSetImpl() {
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAttributeCount() {
        return attributeSet.size();
    }

    @Override
    public String getAttributeValue(String attribute) {
        return Util.toString(attributeSet.get(attribute));
    }

    @Override
    public String getAttributeValue(String attribute, Object defaultValue) {
        Object value = attributeSet.get(attribute);
        if (value != null) {
            return value.toString();
        }
        return Util.toString(defaultValue);
    }

    @Override
    public boolean getBooleanValue(String attribute, boolean defaultValue) {
        Object value = attributeSet.get(attribute);
        if (value != null) {
            return Util.toBoolean(value);
        }
        return defaultValue;
    }

    @Override
    public int getIntValue(String attribute, int defaultValue) {
        Object value = attributeSet.get(attribute);
        if (value != null) {
            return Util.toInt(value);
        }
        return defaultValue;
    }

    @Override
    public float getFloatValue(String attribute, float defaultValue) {
        Object value = attributeSet.get(attribute);
        if (value != null) {
            return Util.toFloat(value);
        }
        return defaultValue;
    }

    @Override
    public List<AttributeSet> getAttributeSets() {
        return attributeSets;
    }

    public void addAttributeSet(AttributeSet attributeSet) {
        this.attributeSets.add(attributeSet);
    }

    public void setParent(AttributeSetImpl attributeSet) {
        parentAttr = attributeSet;
    }

    public AttributeSetImpl getParent() {
        return parentAttr;
    }
}
