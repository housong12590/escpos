package com.hstmpl.escpos.exception;

/**
 * @author hous
 * <p>
 * 打印超时
 */
public class PrintTimeoutException extends RuntimeException {

    public PrintTimeoutException() {
    }

    public PrintTimeoutException(String message) {
        super(message);
    }
}
