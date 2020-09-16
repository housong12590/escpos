package com.hstmpl.escpos.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression {

    private static final Pattern ARRAY_PATTERN = Pattern.compile("(\\w+)\\[(-?\\d+)\\]");

    private String expression;

    private $Key[] keys;

    public static Expression of(String expression) {
        if (expression == null) {
            throw new RuntimeException("expression can not null");
        }
        return new Expression(expression);
    }

    private Expression(String expression) {
        this.expression = expression;
        compile();
    }

    public void compile() {
        String[] split = expression.split("\\.");
        keys = new $Key[split.length];
        for (int i = 0; i < split.length; i++) {
            keys[i] = generateKey(split[i]);
        }
    }

    public $Key[] keys() {
        return keys;
    }

    public String getExpression() {
        return expression;
    }

    public <T> T getValue(Object env) {
        Object tmpEnv = env;
        for ($Key key : keys) {
            if (tmpEnv == null) {
                return null;
            }
            tmpEnv = getValue(key, tmpEnv);
        }
        return cast(tmpEnv);
    }

    private Object getValue($Key key, Object env) {
        Object value;
        if (env instanceof Map) {
            value = ((Map<?, ?>) env).get(key.key);
        } else {
            value = getBeanValue(env, key.key);
        }
        if (key.isArray && value instanceof List) {
            value = getListValue((List<?>) value, key.index);
        }
        return value;
    }

    private Object getListValue(List<?> list, int index) {
        if (index < 0) {
            index = list.size() + index;
        }
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    private Object getBeanValue(Object source, String fieldName) {
        try {
            Field field = source.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(source);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    private $Key generateKey(String keyStr) {
        $Key key = new $Key();
        Matcher matcher = ARRAY_PATTERN.matcher(keyStr);
        if (matcher.matches()) {
            key.isArray = true;
            key.key = matcher.group(1);
            key.index = Integer.parseInt(matcher.group(2));
        } else {
            key.key = keyStr;
        }
        return key;
    }

    public <T> T cast(Object obj) {
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    public static class $Key {

        private String key;
        private boolean isArray;
        private int index;

    }

    @Override
    public String toString() {
        return expression;
    }
}
