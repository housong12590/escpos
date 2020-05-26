package com.xiaom.pos4j.parser;


import com.xiaom.pos4j.common.Dict;
import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.attr.AttributeSet;

/**
 * @author hous
 */
public interface Parser {

    void parser(AttributeSet attrs, Dict data) throws TemplateParseException;
}
