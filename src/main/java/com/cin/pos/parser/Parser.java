package com.cin.pos.parser;


import com.cin.pos.parser.attr.AttributeSet;

import java.util.Map;

public interface Parser {

    void parser(AttributeSet attrs, Map<String, Object> data);
}
