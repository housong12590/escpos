package com.cin.pos.connect;

import com.cin.pos.exception.ConnectException;

public interface Connection {

    void doConnect() throws ConnectException;

    boolean isConnect();

    void write(byte[] bytes) throws ConnectException;

    void flush() throws ConnectException;

    int read(byte[] bytes) throws ConnectException;

    void close();
}
