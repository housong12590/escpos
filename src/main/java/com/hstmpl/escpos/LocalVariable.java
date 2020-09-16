package com.hstmpl.escpos;

import com.hstmpl.escpos.util.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hous
 */
public class LocalVariable {

    private static Map<String, Variable> localVariableSet = new HashMap<>();

    static {
        for (VariableEnum value : VariableEnum.values()) {
            localVariableSet.put(value.name(), value);
        }
    }

    public static void put(String key, Variable variable) {
        localVariableSet.put(key, variable);
    }

    public static boolean contains(String key) {
        return localVariableSet.containsKey(key);
    }

    public static String getValue(String key) {
        Variable variable = localVariableSet.get(key);
        if (variable != null) {
            return variable.getValue();
        }
        return null;
    }


    interface Variable {
        String getValue();
    }

    enum VariableEnum implements Variable {
        now_date {
            @Override
            public String getValue() {
                return DateUtils.getDefaultFormat().format(new Date());
            }
        };

        @Override
        public String getValue() {
            return null;
        }
    }
}
