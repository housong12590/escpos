package com.xiaom.pos4j.v3;

import java.util.List;

public class Properties {

    private String name;

    private String value;

    private List<Placeholder> placeholders;

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
            sb.replace(start, end, p.getVariable().execute(transform, env));
        }
        return sb.toString();
    }
}
