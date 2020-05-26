package com.xiaom.pos4j.connect;

import com.xiaom.pos4j.Constants;
import com.xiaom.pos4j.util.IOUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author hous
 */
public class SocketConnection extends AbstractConnection {

    private static final int bufferSize = 4096;
    private String hostname;
    private int port;
    private int timeout;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private boolean useBuffer;

    public SocketConnection(String hostname) {
        this(hostname, Constants.PRINTER_PORT, Constants.SOCKET_TIMEOUT, false);
    }

    public SocketConnection(String hostname, int port, int timeout, boolean useBuffer) {
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
        this.useBuffer = useBuffer;
    }

    @Override
    public void connect() throws IOException {
        socket = new Socket();
        // 设置连接超时时间
        socket.connect(new InetSocketAddress(hostname, port), timeout);
        socket.setSoTimeout(timeout);
        is = socket.getInputStream();
        if (useBuffer) {
            os = new BufferedOutputStream(socket.getOutputStream(), bufferSize);
        } else {
            os = socket.getOutputStream();
        }
    }

    @Override
    public InputStream getInputStream() {
        return is;
    }

    @Override
    public OutputStream getOutputStream() {
        return os;
    }

    @Override
    public void close() {
        super.close();
        IOUtils.safeClose(socket);
    }

    @Override
    public String toString() {
        return this.hostname + ":" + this.port;
    }
}
