package com.hstmpl.escpos.parser;

public interface Transform {

    <T> T get(String key, Object env);
}
