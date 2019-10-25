package com.ciin.pos.parser;


import com.ciin.common.Dict;
import com.ciin.common.utils.ExpressionUtils;
import com.ciin.pos.Constants;
import com.ciin.pos.LocalVariable;
import com.ciin.pos.convert.ConverterKit;
import com.ciin.pos.element.Document;
import com.ciin.pos.element.Element;
import com.ciin.pos.exception.DissatisfyConditionError;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.StringUtils;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;

import javax.xml.parsers.SAXParser;

public class Template {

    private SAXParser saxParser;
    private String templateStr;
    private Dict data;


    public Template(String templateStr) {
        this(templateStr, null);
    }

    public Template(String templateStr, Dict data) {
        this.templateStr = templateStr;
        this.data = data;
        this.saxParser = XmlParseFactory.newParser();
    }

    public void setTemplateStr(String templateStr) {
        this.templateStr = templateStr;
    }

    public Document toDocument() throws TemplateParseException {
        if (StringUtils.isEmpty(templateStr)) {
            LogUtils.debug("模版内容为空");
            throw new TemplateParseException("模版内容为空");
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
                } catch (DissatisfyConditionError e) {
//                    LogUtils.info(e.getMessage());
                }
            }
        }
        return document;
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
