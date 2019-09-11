package com.ciin.pos.parser;


import com.ciin.pos.common.Dict;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.parser.attr.AttributeSet;

public interface Parser {

    void parser(AttributeSet attrs, Dict data) throws TemplateParseException;
}
