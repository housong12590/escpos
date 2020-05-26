package com.xiaom.pos4j.common;


import com.xiaom.pos4j.util.BeanUtils;
import com.xiaom.pos4j.util.ExpressionUtils;
import com.xiaom.pos4j.util.JSONUtils;
import com.xiaom.pos4j.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author hous
 */
public class Dict implements Map<String, Object> {

    private Map<String, Object> map;

    public Dict() {
        this.map = new LinkedHashMap<>();
    }

    public static Dict create() {
        return new Dict();
    }

    public static Dict create(String json) {
        return new Dict(json);
    }

    public static Dict create(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            return new Dict(map);
        }
        return new Dict(obj);
    }

    public Dict(String json) {
        this.map = JSONUtils.toBean(json, Dict.class);
    }

    public Dict(Map<?, ?> map) {
        this.map = new LinkedHashMap<>();
        Set<? extends Entry<?, ?>> entries = map.entrySet();
        for (Entry<?, ?> entry : entries) {
            this.map.put(entry.getKey().toString(), entry.getValue());
        }
    }

    public Dict(Object bean) {
        this.map = BeanUtils.toMap(bean);
    }

    public void setQueryString(String queryString) {
        Map<String, Object> queryMap = StringUtils.parseQueryString(queryString);
        queryMap.forEach(Dict.this::put);
    }

    public String toQueryString() {
        return StringUtils.toQueryString(this.map);
    }

    public String toJson() {
        return JSONUtils.toJson(this.map);
    }

    public String getString(String key) {
        return getString(key, null);
    }


    public String getString(String key, String defValue) {
        Object v = this.map.get(key);
        if (v == null) {
            return defValue;
        }
        if (v instanceof String) {
            return (String) v;
        }
        return v.toString();
    }


    public String getStringEx(String key) {
        return getStringEx(key, null);
    }

    public String getStringEx(String key, String msg) {
        checkKeyExists(key, msg);
        return getString(key);
    }


    public String getStringExp(String exp) {
        return getStringExp(exp, null);
    }

    public String getStringExp(String exp, String defValue) {
        Object value = getExpressionValue(exp);
        if (value == null) {
            return defValue;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }


    public Integer getInt(String key) {
        return getInt(key, null);
    }

    public Integer getInt(String key, Integer defValue) {
        Object v = this.map.get(key);
        if (v == null) {
            return defValue;
        }
        if (v instanceof Integer) {
            return (Integer) v;
        }
        try {
            return (int) Double.parseDouble(v.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public Integer getIntEx(String key) {
        return getIntEx(key, null);
    }

    public Integer getIntEx(String key, String msg) {
        checkKeyExists(key, msg);
        return getInt(key);
    }

    public Integer getIntExp(String expression, Integer defValue) {
        Object value = getExpressionValue(expression);
        if (value == null) {
            return defValue;
        }
        try {
            return (int) Double.parseDouble(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public Double getDouble(String key, Double defValue) {
        Object v = this.map.get(key);
        if (v == null) {
            return defValue;
        }
        if (v instanceof Double) {
            return (Double) v;
        }
        try {
            return Double.parseDouble(v.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public Double getDoubleEx(String key) {
        return getDoubleEx(key, null);
    }

    public Double getDoubleEx(String key, String msg) {
        checkKeyExists(key, msg);
        return getDouble(key);
    }

    public Double getDoubleExp(String expression) {
        return getDoubleExp(expression, null);
    }

    public Double getDoubleExp(String expression, Double defValue) {
        Object value = ExpressionUtils.getExpressionValue(this, expression);
        if (value == null) {
            return defValue;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }


    public Float getFloat(String key) {
        return getFloat(key, null);
    }

    public Float getFloat(String key, Float defValue) {
        Object v = this.map.get(key);
        if (v == null) {
            return defValue;
        }
        if (v instanceof Float) {
            return (Float) v;
        }
        try {
            return Float.parseFloat(v.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public Float getFloatEx(String key) {
        return getFloatEx(key, null);
    }

    public Float getFloatEx(String key, String msg) {
        checkKeyExists(key, msg);
        return getFloat(key);
    }

    public Boolean getBool(String key) {
        return getBool(key, false);
    }

    public Float getFloatExp(String expression) {
        return getFloatExp(expression, null);
    }

    public Float getFloatExp(String expression, Float defValue) {
        Object value = ExpressionUtils.getExpressionValue(this, expression);
        if (value == null) {
            return defValue;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        try {
            return Float.parseFloat(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public Boolean getBool(String key, Boolean defValue) {
        Object v = this.map.get(key);
        if (v == null) {
            return defValue;
        }
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        try {
            return Boolean.parseBoolean(v.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public Boolean getBoolEx(String key) {
        return getBoolEx(key, null);
    }

    public Boolean getBoolEx(String key, String msg) {
        checkKeyExists(key, msg);
        return getBool(key);
    }

    public BigDecimal getBigDecimal(String key) {
        return getBigDecimal(key, null);
    }


    public BigDecimal getBigDecimal(String key, BigDecimal decimal) {
        Object v = this.map.get(key);
        if (v == null) {
            return decimal;
        }
        if (v instanceof BigDecimal) {
            return (BigDecimal) v;
        }
        try {
            return new BigDecimal(v.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decimal;
    }

    public BigDecimal getBigDecimalEx(String key) {
        return getBigDecimalEx(key, null);
    }

    public BigDecimal getBigDecimalEx(String key, String msg) {
        checkKeyExists(key, msg);
        return getBigDecimal(key);
    }


    public Dict getDict(String key) {
        Object value = this.map.get(key);
        if (value instanceof Dict) {
            return (Dict) value;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return Dict.create(map);
        }
        return new Dict(value);
    }

    public Dict getDictEx(String key, String msg) {
        checkKeyExists(key, msg);
        return getDict(key);
    }

    public Dict sortKey() {
        Set<String> keys = this.map.keySet();
        List<String> keysList = new ArrayList<>(keys);
        keysList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        Dict dict = new Dict();
        for (String s : keysList) {
            dict.put(s, this.map.get(s));
        }
        return dict;
    }

    public Object getExpressionValue(String expression) {
        return ExpressionUtils.getExpressionValue(this, expression);
    }


    @Override
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return this.map.get(key);
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return this.map.getOrDefault(key, defaultValue);
    }

    @Override
    public Object put(String key, Object value) {
        return this.map.put(key, value);
    }

    public Dict set(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public Object putIgnoreNull(String key, Object value) {
        if (value != null) {
            return this.put(key, value);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        this.map.putAll(m);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        return this.map.putIfAbsent(key, value);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override
    public Object remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return this.map.remove(key, value);
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }


    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        return this.map.replace(key, oldValue, newValue);
    }

    @Override
    public Object replace(String key, Object value) {
        return this.map.replace(key, value);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
        this.map.replaceAll(function);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        this.map.forEach(action);
    }


    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        return this.map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return this.map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return this.map.compute(key, remappingFunction);
    }

    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return this.map.merge(key, value, remappingFunction);
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    private void checkKeyExists(String key, String msg) {
        if (!this.map.containsKey(key)) {
            throw new KeyErrorException(msg == null ? String.format("%s 属性不存在", key) : msg);
        }
    }


    public static class KeyErrorException extends RuntimeException {

        private static final long serialVersionUID = 2843905076377664046L;

        KeyErrorException(String message) {
            super(message);
        }
    }
}