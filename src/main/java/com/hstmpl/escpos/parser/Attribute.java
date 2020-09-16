package com.hstmpl.escpos.parser;

import java.util.Map;

public class Attribute implements Map.Entry<String, String> {

    private String key;

    private String value;

    public Attribute(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String old = this.value;
        this.value = value;
        return old;
    }

}
