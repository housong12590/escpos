package com.hstmpl.escpos.exception;

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
