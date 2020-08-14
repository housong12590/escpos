package com.xiaom.pos4j.comm;


import com.xiaom.pos4j.util.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        if (obj == null) {
            return new Dict();
        }
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return new Dict(map);
        }
        return new Dict(obj);
    }

    public Dict(String json) {
        if (StringUtils.isEmpty(json)) {
            this.map = new HashMap<>();
        }
        this.map = JSONUtils.toBean(json, Dict.class);
    }

    public Dict(Map<?, ?> map) {
        this.map = new HashMap<>();
        Set<? extends Entry<?, ?>> entries = map.entrySet();
        for (Entry<?, ?> entry : entries) {
            this.map.put(entry.getKey().toString(), entry.getValue());
        }
    }

    public Dict(Object bean) {
        this.map = BeanUtils.toMap(bean);
    }

    public Dict setQueryString(String queryString) {
        Map<String, Object> queryMap = StringUtils.parseQueryString(queryString);
        queryMap.forEach(Dict.this::put);
        return this;
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

    public Boolean getBool(String key) {
        return getBool(key, false);
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

    public Long getLong(String key) {
        return getLong(key, 0L);
    }

    public Long getLong(String key, Long defValue) {
        Object v = this.map.get(key);
        if (v == null) {
            return defValue;
        }
        if (v instanceof Long) {
            return (Long) v;
        }
        try {
            return Long.parseLong(v.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }


    public Dict getDict(String key) {
        return this.getDict(key, null);
    }

    public Dict getDict(String key, Dict defValue) {
        Object value = this.map.get(key);
        if (value == null) {
            if (defValue != null) {
                this.map.put(key, defValue);
                return defValue;
            }
            return null;
        }
        if (value instanceof Dict) {
            return (Dict) value;
        } else if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            return Dict.create(map);
        }
        return new Dict(value);
    }

    public List getList(String key) {
        Object value = this.map.get(key);
        if (value instanceof List) {
            return (List) value;
        }
        return null;
    }

    public <T> List<T> getList(String key, Class<T> cls) {
        Object value = this.map.get(key);
        if (value instanceof List) {
            List<T> _list = new ArrayList<>();
            ((List<?>) value).forEach(o -> {
                T t = BeanUtils.toBean((Map<?, ?>) o, cls);
                _list.add(t);
            });
            return _list;
        }
        return null;
    }

    public <T> T getBean(String key, Class<T> cls) {
        Object value = this.map.get(key);
        if (value == null) {
            return null;
        }
        return BeanUtils.toBean((Map<?, ?>) value, cls);
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

    public Object getValue(String exp) {
        return ExpressionUtils.getValue(this, exp);
    }

    public <T> T getValue(String expression, T defValue) {
        Object value = this.getValue(expression, defValue.getClass());
        if (value == null) {
            return defValue;
        }
        return (T) value;
    }

    public <T> T getValue(String exp, Class<T> cls) {
        Object value = getValue(exp);
        if (value == null) {
            return null;
        }
        if (ClassUtils.isPrimitive(value)) {
            return ConvertUtils.matchTypeCast(cls, value);
        }
        if (cls == Dict.class) {
            return (T) Dict.create(value);
        }
        if (Map.class.isAssignableFrom(cls)) {
            if (value instanceof Map) {
                return (T) value;
            }
            return (T) BeanUtils.toMap(value);
        }
        if (List.class.isAssignableFrom(cls)) {
            return (T) value;
        }
        if (value instanceof Map) {
            return BeanUtils.toBean((Map<?, ?>) value, cls);
        }
        return BeanUtils.toBean(Dict.create(value), cls);
    }

    public void setValue(String exp, Object value) {
        ExpressionUtils.setValue(this, exp, value);
    }

    public void removeValue(String exp, boolean autoClear) {
        int index = exp.lastIndexOf(".");
        if (index == -1) {
            this.remove(exp);
            return;
        }
        String key = exp.substring(index + 1);
        exp = exp.substring(0, index);
        Object value = getValue(exp);
        if (value instanceof Map) {
            ((Map<?, ?>) value).remove(key);
            if (autoClear && ((Map<?, ?>) value).isEmpty()) {
                removeValue(exp, true);
            }
        }
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
}