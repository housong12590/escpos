package com.ciin.pos.exception;

/**
 * 不满足解析条件异常
 */
public class DissatisfyConditionError extends RuntimeException {

    public DissatisfyConditionError() {
    }

    public DissatisfyConditionError(String message) {
        super(message);
    }
}
