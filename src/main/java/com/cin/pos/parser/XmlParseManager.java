package com.cin.pos.parser;

import javax.xml.parsers.SAXParserFactory;

public class XmlParseManager {

    private static SAXParserFactory saxParserFactory;

    public static SAXParserFactory getParserFactory() {
        if (saxParserFactory == null) {
            saxParserFactory = SAXParserFactory.newInstance();
        }
        return saxParserFactory;
    }
}
