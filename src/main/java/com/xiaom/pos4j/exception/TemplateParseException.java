package com.xiaom.pos4j.exception;

/**
 * 打印模版解析异常
 *
 * @author hous
 */
public class TemplateParseException extends Exception {

    public TemplateParseException() {
    }

    public TemplateParseException(String message) {
        super(message);
    }

    public TemplateParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
