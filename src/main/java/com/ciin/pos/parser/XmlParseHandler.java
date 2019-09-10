package com.ciin.pos.parser;


import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.parser.attr.AttributeSetImpl;

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
            parentAttributeSet = rootAttributeSet = attribute;
        } else {
            attribute.setParent(parentAttributeSet);
            parentAttributeSet.addAttributeSet(attribute);
        }
        parentAttributeSet = currentAttributeSet = attribute;
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
