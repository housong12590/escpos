package com.ciin.pos.connect;

import com.ciin.pos.exception.ConnectionException;

public interface Connection {

    void doConnect() throws ConnectionException;

    boolean isConnect();

    void write(byte[] bytes) throws ConnectionException;

    void flush() throws ConnectionException;

    int read(byte[] bytes) throws ConnectionException;

    void close();
}
