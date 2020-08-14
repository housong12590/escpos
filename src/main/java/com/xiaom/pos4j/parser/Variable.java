package com.xiaom.pos4j.parser;

import com.xiaom.pos4j.util.ClassUtils;
import com.xiaom.pos4j.v3.Transform;

public class Variable {

    private String variable;

    public static Variable of(String variable) {
        return new Variable(variable);
    }

    public Variable(String variable) {
        this.variable = variable;
    }


    public String getVariable() {
        return variable;
    }

    public <T> T execute(Transform transform, Object env) {
        if (transform == null || env == null) {
            return null;
        }
        Object value = transform.get(variable, env);
        if (value == null) {
            return null;
        }
        return ClassUtils.cast(value);
    }

    @Override
    public String toString() {
        return variable;
    }
}
