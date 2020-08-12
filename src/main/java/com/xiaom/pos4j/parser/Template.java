package com.xiaom.pos4j.parser;


import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.LocalVariable;
import com.xiaom.pos4j.comm.Dict;
import com.xiaom.pos4j.convert.ConverterKit;
import com.xiaom.pos4j.element.Document;
import com.xiaom.pos4j.element.Element;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.exception.UnsatisfiedConditionException;
import com.xiaom.pos4j.parser.attr.AttributeSet;
import com.xiaom.pos4j.util.ExpressionUtils;
import com.xiaom.pos4j.util.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author hous
 */
public class Template {

    private SAXParser saxParser;
    private String templateStr;
    private Map<?, ?> data;


    public Template(String templateStr) {
        this(templateStr, null);
    }

    public Template(String templateStr, Object data) {
        this.templateStr = templateStr;
        if (data != null) {
            if (data instanceof Map) {
                this.data = (Map<?, ?>) data;
            } else {
                this.data = Dict.create(data);
            }
        }
        this.saxParser = XmlParseFactory.newParser();
    }

    public void setTemplateStr(String templateStr) {
        this.templateStr = templateStr;
    }

    public Document toDocument() throws TemplateParseException {
        if (StringUtils.isEmpty(templateStr)) {
            throw new TemplateParseException("模版内容为空");
        }
        // 模版预处理,替换模版里的占位符 如 : ${keys}  注: #{keys}在这里暂不处理
        templateStr = pretreatment(templateStr, data);
        Document document = new Document();
        // 解析xml属性
        AttributeSet attributeSet = parserXmlTemplate(templateStr);
        // 遍历解析到的所有节点
        for (AttributeSet attr : attributeSet.getAttributeSets()) {
            // 根据节点名称创建对应的对象元素
            String elementName = attr.getName().toLowerCase();
            Element element = ConverterKit.newElement(elementName);
            if (element != null) {
                try {
                    element.parser(attr, data);
                    // 解析到的节点添加到文档流中
                    document.addElement(element);
                    // 忽略条件不满足异常
                } catch (UnsatisfiedConditionException ignored) {

                }
            }
        }
        return document;
    }


    private String pretreatment(String templateStr, Map<?, ?> data) {
        Matcher matcher = Constants.REPLACE_PATTERN.matcher(templateStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group(1);
            Object obj = ExpressionUtils.getValue(data, expression);
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
