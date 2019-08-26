package com.cin.pos.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlParseFactory {

    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();


    public static SAXParser newParser() {
        try {
            return PARSER_FACTORY.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }
}
