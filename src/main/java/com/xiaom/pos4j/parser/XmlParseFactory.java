package com.xiaom.pos4j.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;

/**
 * @author hous
 */
public class XmlParseFactory {

    private static final SAXParserFactory PARSER_FACTORY = SAXParserFactory.newInstance();

    private static final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");


    public static SAXParser newParser() {
        try {
            return PARSER_FACTORY.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Schema newSchema(InputStream is) {
        Source schemaSource = new StreamSource(is);
        try {
            return SCHEMA_FACTORY.newSchema(schemaSource);
        } catch (SAXException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
