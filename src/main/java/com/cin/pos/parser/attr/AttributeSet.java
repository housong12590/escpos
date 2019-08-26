package com.cin.pos.parser.attr;

import java.util.List;

public interface AttributeSet {

    String getName();

    boolean hasAttribute(String attributeName);

    int getAttributeCount();

    String getAttributeValue(String attribute);

    String getAttributeValue(String attribute, Object defaultValue);

    boolean getBooleanValue(String attribute, boolean defaultValue);

    int getIntValue(String attribute, int defaultValue);

    float getFloatValue(String attribute, float defaultValue);

    List<AttributeSet> getAttributeSets();
}
