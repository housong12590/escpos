package com.cin.pos;

import com.cin.pos.util.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LocalVariable {

    private static Map<String, Variable> variableSet = new HashMap<>();


    static {
        put("date", new DateVariable());
    }

    public static void put(String key, Variable variable) {
        key = key.toLowerCase();
        variableSet.put(key, variable);
    }

    public static boolean contains(String key) {
        key = key.toLowerCase();
        return variableSet.containsKey(key);
    }

    public static String getValue(String key) {
        key = key.toLowerCase();
        Variable variable = variableSet.get(key);
        if (variable != null) {
            return variable.getValue();
        }
        return null;
    }


    interface Variable {
        String getValue();
    }


    static class DateVariable implements Variable {

        @Override
        public String getValue() {
            return Utils.getDefaultFormat().format(new Date());
        }
    }


}
