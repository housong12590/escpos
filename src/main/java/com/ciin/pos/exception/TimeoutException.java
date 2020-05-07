package com.ciin.pos.exception;

public class TimeoutException extends RuntimeException {

    public TimeoutException() {
    }

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
