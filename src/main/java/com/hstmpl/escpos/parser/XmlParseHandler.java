package com.hstmpl.escpos.parser;


import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;


/**
 * xml解析处理器
 *
 * @author hous
 */
public class XmlParseHandler extends DefaultHandler {

    private AttributeSet rootAttributeSet;
    private AttributeSet parentAttributeSet;
    private AttributeSet currentAttributeSet;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        AttributeSet attribute = new AttributeSet(qName);
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            attribute.put(name, value);
        }
        if (rootAttributeSet == null) {
            parentAttributeSet = rootAttributeSet = attribute;
        } else {
            attribute.setParent(parentAttributeSet);
            parentAttributeSet.addChildren(attribute);
        }
        parentAttributeSet = currentAttributeSet = attribute;
    }


    @Override
    public void endElement(String uri, String localName, String qName) {
        if (currentAttributeSet.getParent() != null) {
            if (currentAttributeSet.getElementName().equals(currentAttributeSet.getParent().getElementName())) {
                currentAttributeSet = currentAttributeSet.getParent();
            }
        }
        if (currentAttributeSet != null) {
            while (!currentAttributeSet.getElementName().equals(qName)) {
                currentAttributeSet = parentAttributeSet;
            }
            parentAttributeSet = currentAttributeSet.getParent();
        }
    }


    public AttributeSet getRootAttributeSet() {
        return rootAttributeSet;
    }
}
