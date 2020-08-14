package com.xiaom.pos4j.parser;

import com.xiaom.pos4j.util.ConvertUtils;
import com.xiaom.pos4j.v3.Transform;

import java.util.List;

public class Property {

    private String name;

    private String value;

    private List<Placeholder> placeholders;

    public Property(String name, String value, List<Placeholder> placeholders) {
        this.name = name;
        this.value = value;
        this.placeholders = placeholders;
    }

    public String getName() {

        return name;
    }

    public String getValue() {
        return value;
    }

    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public String apply(Transform transform, Object env) {
        if (placeholders == null || placeholders.isEmpty()) {
            return value;
        }
        StringBuilder sb = new StringBuilder(value);
        for (int i = placeholders.size() - 1; i >= 0; i--) {
            Placeholder p = placeholders.get(i);
            int start = p.getStart();
            int end = p.getEnd();
            Object value = p.getVariable().execute(transform, env);
            if (value != null) {
                sb.replace(start, end, ConvertUtils.toString(value));
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", placeholders=" + placeholders +
                '}';
    }
}
