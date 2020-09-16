package com.hstmpl.escpos.exception;

/**
 * @author hous
 * <p>
 * 不满足条件异常
 */
public class UnsatisfiedConditionException extends RuntimeException {

    public UnsatisfiedConditionException() {
    }

    public UnsatisfiedConditionException(String message) {
        super(message);
    }
}
