package com.cin.pos.parser;


import com.cin.pos.parser.attr.AttributeSet;
import com.cin.pos.parser.attr.AttributeSetImpl;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParseHandler extends DefaultHandler {

    private AttributeSetImpl rootAttributeSet;
    private AttributeSetImpl parentAttributeSet;
    private AttributeSetImpl currentAttributeSet;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        AttributeSetImpl attribute = new AttributeSetImpl(qName);
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            attribute.put(name, value);
        }
        if (rootAttributeSet == null) {
            rootAttributeSet = attribute;
            parentAttributeSet = rootAttributeSet;
        } else {
            attribute.setParent(parentAttributeSet);
            parentAttributeSet.addAttributeSet(attribute);
        }
        currentAttributeSet = attribute;
        parentAttributeSet = currentAttributeSet;
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentAttributeSet.getParent() != null) {
            if (currentAttributeSet.getName().equals(currentAttributeSet.getParent().getName())) {
                currentAttributeSet = currentAttributeSet.getParent();
            }
        }
        if (currentAttributeSet != null) {
            while (!currentAttributeSet.getName().equals(qName)) {
                currentAttributeSet = parentAttributeSet;
            }
            parentAttributeSet = currentAttributeSet.getParent();
        }
    }


    public AttributeSet getRootAttributeSet() {
        return rootAttributeSet;
    }
}
