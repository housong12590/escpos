package com.ciin.pos.element;

import com.ciin.pos.Constants;
import com.ciin.pos.common.Dict;
import com.ciin.pos.convert.ConverterKit;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;
import com.ciin.pos.util.ExpressionUtils;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Group extends Element {

    private String repeatKey;
    private List<Element> children = new ArrayList<>();

    @Override
    public void parser(AttributeSet attrs, Dict data) throws TemplateParseException {
        super.parser(attrs, data);
        this.repeatKey = attrs.getAttributeValue("repeatKey", null);
        String expression = ExpressionUtils.getExpression(Constants.PARSE_PATTERN, this.repeatKey);
        if (StringUtils.isEmpty(expression)) {
            throw new TemplateParseException("表达式格式不合法, 正确表达式如下 #{value} or #{ data.users } ");
        }
        Object value = data.getExpressionValue(expression);
        if (value == null) {
            throw new TemplateParseException(String.format("找不到%s的值, 检查表达式是否正确", expression));
        }
        List _lists;
        if (value instanceof List) {
            _lists = (List) value;
        } else {
            _lists = Collections.singletonList(value);
        }
        repeatChild(_lists, attrs);
    }

    private void repeatChild(List list, AttributeSet attrs) throws TemplateParseException {
        List<AttributeSet> attributeSets = attrs.getAttributeSets();
        for (Object item : list) {
            if (item instanceof Map) {
                Dict itemData = Dict.create(item);
                for (AttributeSet attributeSet : attributeSets) {
                    generateChildElement(attributeSet, itemData);
                }
            } else {
                LogUtils.error(String.format("数据格式不满足遍历条件 %s", item));
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
