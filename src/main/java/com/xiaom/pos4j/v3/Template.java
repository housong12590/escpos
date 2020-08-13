package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.element.*;
import com.xiaom.pos4j.parser.XmlParseFactory;
import com.xiaom.pos4j.parser.XmlParseHandler1;
import com.xiaom.pos4j.util.FileUtils;
import com.xiaom.pos4j.v3.gen.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Template {

    public static Map<String, Class<? extends Element>> elMap = new HashMap<>();
    public static Map<Class<? extends Element>, Class<? extends Generator>> gMAP = new HashMap<>();

    static {
        elMap.put("text", Text.class);
        elMap.put("section", Section.class);
        elMap.put("table", Table.class);
        elMap.put("image", Image.class);
        elMap.put("group", Group.class);

        gMAP.put(Text.class, TextGenerator.class);
        gMAP.put(Image.class, ImageGenerator.class);
        gMAP.put(Section.class, SectionGenerator.class);
        gMAP.put(Group.class, GroupGenerator.class);
        gMAP.put(Table.class, TableGenerator.class);
    }

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
        AttributeSet attributeSet = parseXmlTemplate(templateStr);
        List<AttributeSet> children = attributeSet.getChildren();
        for (AttributeSet attrSet : children) {
            ElementExample example = parseElement(attrSet);
            elementExamples.add(example);
        }
    }

    private ElementExample parseElement(AttributeSet attributes) {
        ElementExample example = new ElementExample();
        Class<? extends Element> aClass = elMap.get(attributes.getElementName());
        example.setElementClass(aClass);
        List<Property> properties = new ArrayList<>();
        for (Attribute attr : attributes) {
            List<Placeholder> placeholders = parsePlaceHolder(attr.getValue());
            Property property = new Property(attr.getKey(), attr.getValue(), placeholders);
            properties.add(property);
        }
        example.setProperties(properties);
        List<ElementExample> children = new ArrayList<>();
        for (AttributeSet attrSet : attributes.getChildren()) {
            children.add(parseElement(attrSet));
        }
        example.setChildren(children.toArray(new ElementExample[0]));
        return example;
    }

    public Document toDocument(Transform transform, Object env) {
        Document document = new Document();
        for (ElementExample elementExample : elementExamples) {
            Element element = generate(elementExample, transform, env);
            document.addElement(element);
        }
        return document;
    }

    public Element generate(ElementExample example, Transform transform, Object env) {
        Class<? extends Generator> aClass = gMAP.get(example.getElementClass());
        try {
            Generator generator = aClass.newInstance();
            return generator.create(example, transform, env);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
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
        XmlParseHandler1 handler = new XmlParseHandler1();
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
