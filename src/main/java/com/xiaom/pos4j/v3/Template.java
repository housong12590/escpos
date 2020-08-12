package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.parser.XmlParseFactory;
import com.xiaom.pos4j.parser.XmlParseHandler;
import com.xiaom.pos4j.parser.attr.AttributeSet;
import com.xiaom.pos4j.util.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Template {

    private String templateStr;

    private AttributeSet attributeSet;

    public static Template compile(File file) {
        String content = FileUtils.read(file);
        return compile(content);
    }


    public static Template compile(String templateStr) {
        return new Template(templateStr);
    }

    public Template(String templateStr) {
        this.templateStr = templateStr;
        this.attributeSet = parseXmlTemplate(templateStr);
    }


    private AttributeSet parseXmlTemplate(String templateStr) {
        XmlParseHandler handler = new XmlParseHandler();
        InputStream is = new ByteArrayInputStream(templateStr.getBytes());
        SAXParser saxParser = XmlParseFactory.newParser();
        try {
            saxParser.parse(is, handler);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("xml模版解析失败");
        }
        return handler.getRootAttributeSet();
    }
}
