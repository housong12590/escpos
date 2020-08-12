package com.xiaom.pos4j.v3;

import com.xiaom.pos4j.util.ClassUtils;

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
