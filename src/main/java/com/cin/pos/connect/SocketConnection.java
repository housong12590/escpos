package com.cin.pos.connect;


import com.cin.pos.util.Util;

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
    public void doConnect() throws IOException {
        this.close();
        socket = new Socket();
        // 设置连接超时时间
        socket.connect(new InetSocketAddress(hostname, port), timeout);
        os = socket.getOutputStream();
        is = socket.getInputStream();
        bufferOs = new BufferedOutputStream(os, bufferSize);
    }


    @Override
    public boolean isConnect() {
        if (socket != null) {
            try {
                socket.sendUrgentData(0xFF);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    public void write(byte[] bytes) throws IOException {
        if (bufferOs != null) {
            bufferOs.write(bytes);
        }
    }

    @Override
    public void flush() throws IOException {
        if (bufferOs != null) {
            bufferOs.flush();
        }
        if(os != null){
            os.flush();
        }
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        if (is != null) {
            return is.read(bytes);
        }
        throw new IOException("inputStream can not null...");
    }

    @Override
    public void close() {
        Util.ioClose(is, bufferOs, os, socket);
    }

    @Override
    public String toString() {
        return hostname + ":" + port;
    }
}
