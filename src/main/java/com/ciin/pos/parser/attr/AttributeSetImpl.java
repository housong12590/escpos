package com.ciin.pos.parser.attr;


import com.ciin.common.utils.ConvertUtils;

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
    public boolean hasAttribute(String attributeName) {
        return attributeSet.containsKey(attributeName);
    }

    @Override
    public int getAttributeCount() {
        return attributeSet.size();
    }

    @Override
    public String getAttributeValue(String attribute) {
        return ConvertUtils.toString(attributeSet.get(attribute));
    }

    @Override
    public String getAttributeValue(String attribute, Object defaultValue) {
        Object value = attributeSet.get(attribute);
        return ConvertUtils.toString(value, ConvertUtils.toString(defaultValue));
    }

    @Override
    public boolean getBooleanValue(String attribute, boolean defaultValue) {
        Object value = attributeSet.get(attribute);
        return ConvertUtils.toBool(value, defaultValue);
    }

    @Override
    public int getIntValue(String attribute, int defaultValue) {
        Object value = attributeSet.get(attribute);
        return ConvertUtils.toInt(value, defaultValue);
    }

    @Override
    public float getFloatValue(String attribute, float defaultValue) {
        Object value = attributeSet.get(attribute);
        return ConvertUtils.toFloat(value, defaultValue);
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
