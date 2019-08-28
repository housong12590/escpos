package com.cin.pos.connect;

import com.cin.pos.exception.ConnectionException;

public interface Connection {

    void doConnect() throws ConnectionException;

    boolean isConnect();

    void write(byte[] bytes) throws ConnectionException;

    void flush() throws ConnectionException;

    int read(byte[] bytes) throws ConnectionException;

    void close();
}
