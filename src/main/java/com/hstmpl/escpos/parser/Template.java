package com.hstmpl.escpos.parser;

import com.hstmpl.escpos.element.Element;
import com.hstmpl.escpos.util.FileUtils;
import com.hstmpl.escpos.util.StringUtils;
import com.hstmpl.escpos.Constants;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Template {

    private String templateStr;
    private List<ElementExample> elementExamples;

    public static Template compile(File file) {
        String content = FileUtils.read(file);
        return compile(content);
    }


    public static Template compile(String templateStr) {
        return new Template(templateStr);
    }

    public Template(String templateStr) {
        this.elementExamples = new ArrayList<>();
        this.templateStr = templateStr;
        this.compile();
    }

    private void compile() {
        validateXml();
        AttributeSet attributeSet = parseXmlTemplate(templateStr);
        List<AttributeSet> children = attributeSet.getChildren();
        for (AttributeSet attrSet : children) {
            ElementExample example = parseElement(attrSet);
            elementExamples.add(example);
        }
    }

    private ElementExample parseElement(AttributeSet attributes) {
        ElementExample example = new ElementExample();
        Class<? extends Element> aClass = ElementKit.getElementClass(attributes.getElementName());
        example.setElementClass(aClass);
        for (Attribute attr : attributes) {
            List<Placeholder> placeholders = parsePlaceHolder(attr);
            Property property = new Property(attr.getKey(), attr.getValue(), placeholders);
            example.addProperty(property);
        }
        List<ElementExample> children = new ArrayList<>();
        for (AttributeSet attrSet : attributes.getChildren()) {
            children.add(parseElement(attrSet));
        }
        example.setChildren(children.toArray(new ElementExample[0]));
        return example;
    }

    public Document toDocument() {
        return toDocument(null);
    }

    public Document toDocument(Object env) {
        Document document = new Document();
        for (ElementExample elementExample : elementExamples) {
            Element element = ElementKit.getElement(elementExample, MapTransform.get(), env);
            if (element != null) {
                document.addElement(element);
            }
        }
        return document;
    }

    private List<Placeholder> parsePlaceHolder(Attribute attr) {
        List<Placeholder> list = null;
        Matcher matcher = Constants.PARSE_PATTERN.matcher(attr.getValue());
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

    private boolean validateXml() {
        try {
            InputStream xsdStream = StringUtils.string2inputStream(Constants.XSD_CONTENT);
            InputStream xmlInputStream = StringUtils.string2inputStream(templateStr);
            Schema schema = XmlParseFactory.newSchema(xsdStream);
            Validator validator = schema.newValidator();
            Source xmlSource = new StreamSource(xmlInputStream);
            validator.validate(xmlSource);
            return true;
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ElementExample example : elementExamples) {
            sb.append(example.getElementClass());
            List<Property> properties = example.getProperties();
            for (Property property : properties) {
                sb.append(" ").append(property.getName()).append("=").append(property.getValue());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
