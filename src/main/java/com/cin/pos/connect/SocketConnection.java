package com.cin.pos.connect;


import com.cin.pos.exception.ConnectException;
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
    public void doConnect() throws ConnectException {
        this.close();
        try {
            socket = new Socket();
            // 设置连接超时时间
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            bufferOs = new BufferedOutputStream(os, bufferSize);
        } catch (IOException e) {
            throw new ConnectException();
        }
    }


    @Override
    public boolean isConnect() {
        if (socket != null) {
            try {
                socket.sendUrgentData(0xFF);
                return true;
            } catch (IOException ignored) {
            }
        }
        return false;
    }


    @Override
    public void write(byte[] bytes) throws ConnectException {
        if (bufferOs != null) {
            try {
                bufferOs.write(bytes);
            } catch (IOException e) {
                throw new ConnectException();
            }
        }
    }

    @Override
    public void flush() throws ConnectException {
        try {
            if (bufferOs != null) {
                bufferOs.flush();
            }
            if (os != null) {
                os.flush();
            }
        } catch (IOException e) {
            throw new ConnectException();
        }

    }

    @Override
    public int read(byte[] bytes) throws ConnectException {
        if (is != null) {
            try {
                return is.read(bytes);
            } catch (IOException ignored) {
            }
        }
        throw new ConnectException();
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
