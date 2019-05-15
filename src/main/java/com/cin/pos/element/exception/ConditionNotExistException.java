package com.cin.pos.element.exception;

public class ConditionNotExistException extends RuntimeException {

    public ConditionNotExistException() {
    }

    public ConditionNotExistException(String message) {
        super(message);
    }
}
