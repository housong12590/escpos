package com.xiaom.pos4j.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionUtils {

    private final static Pattern ARRAY_PATTERN = Pattern.compile("(\\w+)\\[(-?\\d+)\\]");

    public static String replacePlaceholder(Pattern regexp, String content, Map data) {
        Matcher matcher = regexp.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group(1);
            Object value = getValue(data, expression);
            if (value == null) {
                return expression;
            }
            String valueStr = ConvertUtils.toString(value);
            matcher.appendReplacement(sb, valueStr);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String getExpression(Pattern regexp, String text) {
        Matcher matcher = regexp.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void setValue(Map source, String exp, Object value) {
        if (StringUtils.isEmpty(exp)) {
            return;
        }
        int index = exp.indexOf(".");
        if (index == -1) {
            source.put(exp, value);
            return;
        }
        String key = exp.substring(0, index);
        exp = exp.substring(index + 1);
        Object v = source.computeIfAbsent(key, k -> new HashMap<>());
        Map tempMap = (Map) v;
        setValue(tempMap, exp, value);
    }

    public static Object getValue(Object source, String expression) {
        if (source == null) {
            return null;
        }
        if (StringUtils.isEmpty(expression)) {
            return null;
        }
        int index = expression.indexOf(".");
        String key;
        if (index != -1) {
            key = expression.substring(0, index);
            expression = expression.substring(index + 1);
        } else {
            key = expression;
        }

        boolean isArray = false;
        int arrayIndex = 0;
        Matcher matcher = ARRAY_PATTERN.matcher(key);
        if (matcher.matches()) {
            isArray = true;
            key = matcher.group(1);
            arrayIndex = Integer.parseInt(matcher.group(2));
        }
        Object value;
        if (source instanceof Map) {
            value = ((Map) source).get(key);
        } else {
            value = getObjectField(source, key);
        }
        if (isArray) {
            value = getListItem(value, arrayIndex);
        }
        if (index == -1) {
            return value;
        }
        if (value == null) {
            return null;
        }
        return getValue(value, expression);
    }

    private static Object getListItem(Object source, int index) {
        if (source instanceof List) {
            List _list = (List) source;
            if (index >= 0) {
                if (index < _list.size()) {
                    return _list.get(index);
                }
            } else {
                if (Math.abs(index) < _list.size()) {
                    index = _list.size() + index;
                    return _list.get(index);
                }
            }
        }
        return null;
    }

    private static Object getObjectField(Object source, String fieldName) {
        try {
            Field field = source.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(source);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    private static void setObjectField(Object source, String fieldName, Object value) {
        try {
            Field field = source.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(source, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
