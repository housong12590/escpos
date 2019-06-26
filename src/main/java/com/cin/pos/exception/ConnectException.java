package com.cin.pos.exception;

public class ConnectException extends RuntimeException {


    public ConnectException() {
    }

    public ConnectException(String message) {
        super(message);
    }
}
