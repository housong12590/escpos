package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.element.Document;
import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.element.Text;
import com.xiaom.pos4j.parser.XmlParseFactory;
import com.xiaom.pos4j.parser.XmlParseHandler;
import com.xiaom.pos4j.parser.attr.Attribute;
import com.xiaom.pos4j.parser.attr.AttributeSet;
import com.xiaom.pos4j.util.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Template {

    private String templateStr;

    private List<ElementSample> elementSamples;

    public static Template compile(File file) {
        String content = FileUtils.read(file);
        return compile(content);
    }


    public static Template compile(String templateStr) {
        return new Template(templateStr);
    }

    public Template(String templateStr) {
        this.elementSamples = new ArrayList<>();
        this.templateStr = templateStr;
        this.compile();
    }

    private void compile() {
        AttributeSet attributeSet = parseXmlTemplate(templateStr);
        for (AttributeSet attrSet : attributeSet.getAttributeSets()) {
            ElementSample sample = new ElementSample();
            sample.setElementClass(Text.class);
            List<Property> propertyList = new ArrayList<>();
            for (Attribute attr : attrSet) {
                List<Placeholder> placeholders = parsePlaceHolder(attr.getValue());
                Property property = new Property(attr.getName(), attr.getValue(), placeholders);
                propertyList.add(property);
            }
            sample.setProperties(propertyList);
            elementSamples.add(sample);
        }
    }

    public Document toDocument(Transform transform, Object env) {
        Document document = new Document();
        for (ElementSample elementSample : elementSamples) {
            Element element = generate(elementSample, transform, env);
            document.addElement(element);
        }
        return document;
    }

    public Element generate(ElementSample sample, Transform transform, Object env) {
        TextGenerator generator = new TextGenerator();
        return generator.create(sample, transform, env);
    }


    public List<Placeholder> parsePlaceHolder(String text) {
        Matcher matcher = Const.PARSE_PATTERN.matcher(text);
        List<Placeholder> list = null;
        while (matcher.find()) {
            if (list == null) list = new ArrayList<>();
            int start = matcher.start();
            int end = matcher.end();
            String value = matcher.group(1);
            Placeholder placeholder = new Placeholder(start, end, value);
            list.add(placeholder);
        }
        return list;
    }

    private AttributeSet parseXmlTemplate(String templateStr) {
        XmlParseHandler handler = new XmlParseHandler();
        InputStream is = new ByteArrayInputStream(templateStr.getBytes());
        SAXParser saxParser = XmlParseFactory.newParser();
        try {
            saxParser.parse(is, handler);
        } catch (SAXException | IOException e) {
            throw new RuntimeException("xml模版解析失败: " + e.getMessage());
        }
        return handler.getRootAttributeSet();
    }

    @Override
    public String toString() {
        return elementSamples.toString();
    }
}
