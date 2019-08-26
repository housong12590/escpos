package com.cin.pos.parser;


import com.cin.pos.Constants;
import com.cin.pos.common.Dict;
import com.cin.pos.LocalVariable;
import com.cin.pos.convert.ConverterKit;
import com.cin.pos.element.Document;
import com.cin.pos.element.Element;
import com.cin.pos.element.exception.ConditionNotExistException;
import com.cin.pos.element.exception.TemplateParseException;
import com.cin.pos.parser.attr.AttributeSet;
import com.cin.pos.util.ExpressionUtils;
import com.cin.pos.util.LoggerUtils;
import com.cin.pos.util.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class PrintTemplate {

    private SAXParser saxParser;
    private String templateStr;
    private Dict data;


    public PrintTemplate(String templateStr) {
        this(templateStr, null);
    }

    public PrintTemplate(String templateStr, Dict data) {
        this.templateStr = templateStr;
        this.data = data;
        this.saxParser = XmlParseFactory.newParser();
    }

    public Document toDocument() {
        if (StringUtils.isEmpty(templateStr)) {
            LoggerUtils.debug("模版内容为空");
            throw new NullPointerException("模版内容为空");
        }
        // 模版预处理,替换模版里的占位符 如 : ${keys}  注: #{keys}在这里暂不处理
        templateStr = pretreatment(templateStr, data);
        Document document = new Document();
        AttributeSet attributeSet = parserXmlTemplate(templateStr);
        for (AttributeSet set : attributeSet.getAttributeSets()) {
            String elementName = set.getName().toLowerCase();
            Element element = ConverterKit.newElement(elementName);
            if (element != null) {
                try {
                    element.parser(set, data);
                    document.addElement(element);
                } catch (ConditionNotExistException|TemplateParseException e) {
                    LoggerUtils.info(e.getMessage());
                }
            }
        }
        return document;
    }

    public String preview() {
        Document document = toDocument();
        List<Element> elements = document.getElements();
        StringBuilder sb = new StringBuilder();
        for (Element element : elements) {
            sb.append(element.toString());
        }
        return sb.toString();
    }


    private String pretreatment(String templateStr, Map data) {
        Matcher matcher = Constants.REPLACE_PATTERN.matcher(templateStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group(1);
            Object obj = ExpressionUtils.getExpressionValue(data, expression);
            String value = obj == null ? null : obj.toString();
            if (value == null) {
                value = LocalVariable.getValue(expression);
            }
            if (value == null) {
                value = expression;
            }
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    private AttributeSet parserXmlTemplate(String templateStr) {
        XmlParseHandler handler = new XmlParseHandler();
        InputStream inputStream = new ByteArrayInputStream(templateStr.getBytes());
        try {
            saxParser.parse(inputStream, handler);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return handler.getRootAttributeSet();
    }

}
