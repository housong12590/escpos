package com.ciin.pos.exception;

public class PlatformErrorException extends RuntimeException {

    public PlatformErrorException() {
    }

    public PlatformErrorException(String message) {
        super(message);
    }
}
