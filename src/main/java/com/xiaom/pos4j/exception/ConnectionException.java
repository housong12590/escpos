package com.xiaom.pos4j.exception;

/**
 * @author hous
 * <p>
 * 连接异常
 */
public class ConnectionException extends RuntimeException {

    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }
}
