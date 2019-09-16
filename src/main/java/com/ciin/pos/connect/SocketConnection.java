package com.ciin.pos.connect;


import com.ciin.pos.util.Utils;

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
    public void doConnect() throws IOException {
        this.close();
        try {
            socket = new Socket();
            // 设置连接超时时间
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            socket.setSoTimeout(timeout);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            bufferOs = new BufferedOutputStream(os, bufferSize);
            isConnect = true;
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }


    @Override
    public boolean isConnect() {
        return isConnect;
    }


    @Override
    public void write(byte[] bytes) throws IOException {
        if (bufferOs != null) {
            try {
                bufferOs.write(bytes);
            } catch (IOException e) {
                isConnect = false;
                throw e;
            }
        }
    }

    @Override
    public void writeAndFlush(byte[] data) throws IOException {
        write(data);
        flush();
    }

    @Override
    public int writeAndRead(byte[] wb, byte[] rb) throws IOException {
        writeAndFlush(wb);
        return read(rb);
    }

    @Override
    public void flush() throws IOException {
        try {
            if (bufferOs != null) {
                bufferOs.flush();
            }
            if (os != null) {
                os.flush();
            }
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        if (is != null) {
            try {
                return is.read(bytes);
            } catch (IOException e) {
                isConnect = false;
                throw e;
            }
        }
        return -1;
    }

    @Override
    public void close() {
        isConnect = false;
        Utils.safeClose(is, bufferOs, os, socket);
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }
}
