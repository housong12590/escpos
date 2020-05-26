package com.xiaom.pos4j.element;

import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.common.Dict;
import com.xiaom.pos4j.convert.ConverterKit;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.attr.AttributeSet;
import com.xiaom.pos4j.util.ExpressionUtils;
import com.xiaom.pos4j.util.LogUtils;
import com.xiaom.pos4j.util.StringUtils;

import java.util.*;

/**
 * @author hous
 */
public class Group extends Element {

    private String repeatKey;
    private List<Element> children = new ArrayList<>();

    @Override
    public void parser0(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException {
        this.repeatKey = attrs.getAttributeValue(Attribute.REPEAT_KEY, null);
        if (StringUtils.isEmpty(repeatKey)) {
            return;
        }
        String expression = ExpressionUtils.getExpression(Constants.PARSE_PATTERN, this.repeatKey);
        if (StringUtils.isEmpty(expression)) {
            throw new TemplateParseException("表达式错误: " + repeatKey);
        }
        Object value = ExpressionUtils.getExpressionValue(data, expression);
        if (value == null) {
            throw new TemplateParseException(expression + " 值为null");
        }
        if (!(value instanceof Iterable)) {
            throw new TemplateParseException(expression + "的值不是一个可迭代对象, 无法进行遍历");
        }
        Iterable<?> it = (Iterable<?>) value;
        repeatChild(it, attrs);
    }

    private void repeatChild(Iterable<?> it, AttributeSet attrs) throws TemplateParseException {
        List<AttributeSet> attributeSets = attrs.getAttributeSets();
        for (Object item : it) {
            Dict itemData = Dict.create(item);
            try {
                for (AttributeSet attributeSet : attributeSets) {
                    generateChildElement(attributeSet, itemData);
                }
            } catch (Exception e) {
                LogUtils.error(String.format("数据格式错误 %s", item));
            }
        }
    }

    private void generateChildElement(AttributeSet attrs, Dict data) throws TemplateParseException {
        String elementName = attrs.getName();
        Element element = ConverterKit.newElement(elementName);
        if (element != null) {
            element.parser(attrs, data);
            children.add(element);
        } else {
            LogUtils.error(String.format("%s 标签没有匹配到相应的元素 ", elementName));
        }
    }

    public String getRepeatKey() {
        return repeatKey;
    }

    public void setRepeatKey(String repeatKey) {
        this.repeatKey = repeatKey;
    }

    public List<Element> getChildren() {
        return children;
    }

    public void setChildren(List<Element> children) {
        this.children = children;
    }
}
