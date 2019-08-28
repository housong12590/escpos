package com.cin.pos.connect;


import com.cin.pos.exception.ConnectionException;
import com.cin.pos.util.Utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketConnection implements Connection {

    private static final int bufferSize = 4096;
    private String hostname;
    private int port;
    private int timeout;
    private boolean isConnect;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private BufferedOutputStream bufferOs;

    public SocketConnection(String hostname, int port, int timeout) {
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
    }


    @Override
    public void doConnect() throws ConnectionException {
        this.close();
        try {
            socket = new Socket();
            // 设置连接超时时间
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            bufferOs = new BufferedOutputStream(os, bufferSize);
            isConnect = true;
        } catch (IOException e) {
            isConnect = false;
            throw new ConnectionException();
        }
    }


    @Override
    public boolean isConnect() {
        return isConnect;
    }


    @Override
    public void write(byte[] bytes) throws ConnectionException {
        if (bufferOs != null) {
            try {
                bufferOs.write(bytes);
            } catch (IOException e) {
                isConnect = false;
                throw new ConnectionException();
            }
        }
    }

    @Override
    public void flush() throws ConnectionException {
        try {
            if (bufferOs != null) {
                bufferOs.flush();
            }
            if (os != null) {
                os.flush();
            }
        } catch (IOException e) {
            isConnect = false;
            throw new ConnectionException();
        }

    }

    @Override
    public int read(byte[] bytes) throws ConnectionException {
        if (is != null) {
            try {
                return is.read(bytes);
            } catch (IOException ignored) {
            }
        }
        isConnect = false;
        throw new ConnectionException();
    }

    @Override
    public void close() {
        Utils.safeClose(is, bufferOs, os, socket);
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }
}
