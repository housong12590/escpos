package com.cin.pos.parser;


import com.cin.pos.common.Dict;
import com.cin.pos.element.exception.TemplateParseException;
import com.cin.pos.parser.attr.AttributeSet;

public interface Parser {

    void parser(AttributeSet attrs, Dict data) throws TemplateParseException;
}
