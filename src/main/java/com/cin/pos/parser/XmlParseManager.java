package com.cin.pos.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlParseManager {

    private static SAXParser saxParser;
    private static SAXParserFactory saxParserFactory;

    public static SAXParser getSaxParser() {
        if (saxParserFactory == null) {
            saxParserFactory = SAXParserFactory.newInstance();
        }
        if (saxParser == null) {
            try {
                saxParser = saxParserFactory.newSAXParser();
            } catch (ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        }
        return saxParser;
    }
}
