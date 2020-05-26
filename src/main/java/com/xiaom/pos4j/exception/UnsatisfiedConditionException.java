package com.xiaom.pos4j.exception;

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
