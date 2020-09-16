package com.hstmpl.escpos.comm;


import com.hstmpl.escpos.util.*;
import com.xiaom.pos4j.util.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Dict extends HashMap<String, Object> implements BasicTypeGetter<String> {

    private static final long serialVersionUID = 6431518080541716381L;

    private Map<String, Expression> expressionMap;

    public static Dict of() {
        return new Dict();
    }

    public static Dict of(String key, Object value) {
        return of().set(key, value);
    }

    public static Dict of(Object obj) {
        if (obj == null) return of();
        if (obj instanceof String) {
            return new Dict((String) obj);
        } else if (obj instanceof Map) {
            return new Dict((Map<?, ?>) obj);
        }
        return new Dict(obj);
    }

    public Dict() {

    }

    public Dict(int initialCapacity) {
        super(initialCapacity);
    }

    public Dict(String json) {
        if (StringUtils.isNotEmpty(json)) {
            this.putAll(JSONUtils.toBean(json, Dict.class));
        }
    }

    public Dict(Map<?, ?> map) {
        for (Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            put(key.toString(), entry.getValue());
        }
    }

    public Dict(Object bean) {
        BeanUtils.toMap(bean, this);
    }

    public Dict set(String key, Object value) {
        this.put(key, value);
        return this;
    }

    @Override
    public <T> T getValue(String key, T defValue) {
        if (expressionMap == null) {
            expressionMap = new HashMap<>();
        }
        Expression expression = expressionMap.get(key);
        if (expression == null) {
            expression = Expression.of(key);
            expressionMap.put(key, expression);
        }
        Object value = expression.getValue(this);
        if (value == null) {
            return defValue;
        }
        return ClassUtils.cast(value);
    }

    public String getString(String key, String defValue) {
        return MapUtils.getString(this, key, defValue);
    }

    public Integer getInt(String key, Integer defVal) {
        return MapUtils.getInt(this, key, defVal);
    }

    public Float getFloat(String key, Float defVal) {
        return MapUtils.getFloat(this, key, defVal);
    }

    public Double getDouble(String key, Double defVal) {
        return MapUtils.getDouble(this, key, defVal);
    }

    public Long getLong(String key, Long defVal) {
        return MapUtils.getLong(this, key, defVal);
    }

    public Boolean getBool(String key, Boolean defVal) {
        return MapUtils.getBool(this, key, defVal);
    }

    public <T> T get(String key, Function<Object, T> mapping) {
        Object value = this.getValue(key);
        return mapping.apply(value);
    }

    public String toJson() {
        return JSONUtils.toJson(this);
    }

    public Dict filter(String... keys) {
        Dict result = new Dict(keys.length);
        for (String key : keys) {
            Object o = this.getValue(key);
            if (o != null) result.put(key, o);
        }
        return result;
    }

    public void removeKeys(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
}