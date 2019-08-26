package com.cin.pos.element;

import com.cin.pos.Constants;
import com.cin.pos.convert.ConverterKit;
import com.cin.pos.parser.attr.AttributeSet;
import com.cin.pos.util.LoggerUtils;
import com.cin.pos.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Group extends Element {

    private String repeatKey;
    private List<Element> children = new ArrayList<>();

    @Override
    public void parser(AttributeSet attrs, Map<String, Object> data) {
        super.parser(attrs, data);
        this.repeatKey = attrs.getAttributeValue("repeatKey", null);
        Object value = getExpressionValue(data, repeatKey);
        if (value == null) {
            return;
        }
        List _lists;
        if (value instanceof List) {
            _lists = (List) value;
        } else {
            _lists = Collections.singletonList(value);
        }
        repeatChild(_lists, attrs);
    }


    private Object getExpressionValue(Map data, String expression) {
        Matcher matcher = Constants.PARSE_PATTERN.matcher(expression);
        if (matcher.find()) {
            String key = StringUtils.getPlaceholderKey(Constants.PARSE_PATTERN, expression);
            if (StringUtils.isEmpty(key)) {
                return null;
            }
            Object value = data.get(key);
            if (value == null) {
                LoggerUtils.error(String.format("找不到%s的值, 检查表达式是否正确", expression));
                return null;
            } else {
                return value;
            }
        } else {
            LoggerUtils.error("表达式格式不合法, 正确表达式如下 #{value} or #{ data.users } ");
        }
        return null;
    }

    private void repeatChild(List list, AttributeSet attrs) {
        List<AttributeSet> attributeSets = attrs.getAttributeSets();
        for (Object item : list) {
            if (item instanceof List) {
                repeatChild((List) item, attrs);
            } else if (item instanceof Map) {
                Map<String, Object> itemMap = (Map) item;
                for (AttributeSet attributeSet : attributeSets) {
                    generateChildElement(attributeSet, itemMap);
                }
            } else {
                LoggerUtils.error(String.format("数据格式不满足遍历条件 %s", item));
            }
        }
    }

    private void generateChildElement(AttributeSet attrs, Map data) {
        String elementName = attrs.getName();
        Element element = ConverterKit.newElement(elementName);
        if (element != null) {
            element.parser(attrs, data);
            children.add(element);
        } else {
            LoggerUtils.error(String.format("%s 标签没有匹配到相应的元素 ", elementName));
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
