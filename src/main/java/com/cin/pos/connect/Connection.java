package com.cin.pos.connect;

import java.io.IOException;

public interface Connection {

    void doConnect() throws IOException;

    boolean isConnect();

    void write(byte[] bytes) throws IOException;

    void flush() throws IOException;

    int read(byte[] bytes) throws IOException;

    void close();
}
