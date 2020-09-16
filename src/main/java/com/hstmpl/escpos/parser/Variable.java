package com.hstmpl.escpos.parser;

import com.hstmpl.escpos.LocalVariable;
import com.hstmpl.escpos.util.ClassUtils;

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
            value = LocalVariable.getValue(variable);
        }
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
