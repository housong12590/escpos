package com.xiaom.pos4j.parser;


import com.xiaom.pos4j.exception.TemplateParseException;
import com.xiaom.pos4j.parser.attr.AttributeSet;

import java.util.Map;

/**
 * @author hous
 */
public interface Parser {

    void parser(AttributeSet attrs, Map<?, ?> data) throws TemplateParseException;
}
