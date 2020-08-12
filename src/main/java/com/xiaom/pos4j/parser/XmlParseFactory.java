package com.xiaom.pos4j.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author hous
 */
public class XmlParseFactory {

    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();


    public static SAXParser newParser() {
        try {
            return PARSER_FACTORY.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
