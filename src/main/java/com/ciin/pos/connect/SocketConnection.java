package com.ciin.pos.connect;


import com.ciin.pos.util.LogUtils;
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
    private boolean useBuffer;
    private BufferedOutputStream bufferOs;

    public SocketConnection(String hostname, int port, int timeout, boolean useBuffer) {
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
        this.useBuffer = useBuffer;
    }


    @Override
    public void doConnect() throws IOException {
        this.close();
        try {
            LogUtils.debug("开始连接 " + this);
            socket = new Socket();
            // 设置连接超时时间
            socket.connect(new InetSocketAddress(hostname, port), timeout);
            socket.setSoTimeout(timeout);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            if (useBuffer) {
                bufferOs = new BufferedOutputStream(os, bufferSize);
            }
            isConnect = true;
            LogUtils.debug("连接成功 " + this);
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }

    @Override
    public boolean tryConnect() {
        return tryConnect(null);
    }

    @Override
    public boolean tryConnect(OnConnectCallback callback) {
        if (!isConnect) {
            try {
                doConnect();
                return true;
            } catch (IOException e) {
                LogUtils.error(String.format("连接异常 %s:%s, ex: %s", hostname, port, e.getMessage()));
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        }
        return false;
    }

    @Override
    public boolean isConnect() {
        return isConnect;
    }


    @Override
    public void write(byte[] bytes) throws IOException {
        try {
            if (useBuffer) {
                bufferOs.write(bytes);
            } else {
                os.write(bytes);
            }
        } catch (IOException e) {
            isConnect = false;
            throw e;
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
            if (useBuffer) {
                bufferOs.flush();
            } else {
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
