package com.cin.pos.parser;


import com.cin.pos.Constants;
import com.cin.pos.LocalVariable;
import com.cin.pos.convert.ConverterKit;
import com.cin.pos.element.Document;
import com.cin.pos.element.Element;
import com.cin.pos.element.exception.ConditionNotExistException;
import com.cin.pos.parser.attr.AttributeSet;
import com.cin.pos.util.LoggerUtil;
import com.cin.pos.util.StringUtil;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;

import javax.xml.parsers.SAXParser;

public class PrintTemplate {

    private SAXParser saxParser;

    public PrintTemplate() {
        saxParser = XmlParseFactory.newParser();
    }

    public Document parser(String templateStr, Map data) {
        if (StringUtil.isEmpty(templateStr)) {
            LoggerUtil.debug("模版内容为空");
            throw new NullPointerException("模版内容为空");
        }
        // 模版预处理,替换模版里的占位符 如 : ${keys}  注: #{keys}在这里暂不处理
        templateStr = pretreatment(templateStr, data);
        Document document = new Document();
        AttributeSet attributeSet = parserXmlTemplate(templateStr);
        for (AttributeSet set : attributeSet.getAttributeSets()) {
            String name = set.getName().toLowerCase();
            Element element = ConverterKit.newElement(name);
            if (element != null) {
                try {
                    element.parser(set, data);
                    document.addElement(element);
                } catch (ConditionNotExistException e) {
                    LoggerUtil.info(e.getMessage());
                }
            }
        }
        return document;
    }


    private String pretreatment(String templateStr, Map data) {
        Matcher matcher = Constants.REPLACE_PATTERN1.matcher(templateStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            Object obj = StringUtil.getValue(data, key);
            String value = obj == null ? null : obj.toString();
            if (value == null) {
                value = LocalVariable.getValue(key);
            }
            if (value == null) {
                value = "null";
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
