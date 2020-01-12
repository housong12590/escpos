package com.ciin.pos.exception;

import java.io.IOException;

/**
 * @author hous
 * <p>
 * 蓝牙连接异常
 */
public class BlueToothException extends IOException {

    public BlueToothException() {
    }

    public BlueToothException(String message) {
        super(message);
    }
}
