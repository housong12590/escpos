package com.xiaom.pos4j.connect;

import com.xiaom.pos4j.util.IOUtils;
import com.xiaom.pos4j.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author hous
 */
public abstract class AbstractConnection implements Connection {

    private boolean isConnect;
    private boolean isClose;

    @Override
    public void doConnect() throws IOException {
        try {
            isClose = false;
            connect();
            this.isConnect = true;
            LogUtils.debug("连接成功 " + this);
        } catch (IOException e) {
            this.isConnect = false;
            if (!isClose) {
                LogUtils.error(String.format("连接异常 %s, ex: %s", this, e.getMessage()));
                throw e;
            }
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
    public void write(byte[] data) throws IOException {
        if (data != null) {
            try {
                getOutputStream().write(data);
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
        write(wb);
        flush();
        return read(rb);
    }

    @Override
    public void flush() throws IOException {
        try {
            getOutputStream().flush();
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }

    @Override
    public int read(byte[] buff) throws IOException {
        try {
            return getInputStream().read(buff);
        } catch (IOException e) {
            isConnect = false;
            throw e;
        }
    }

    @Override
    public void close() {
        this.isClose = true;
        if (this.isConnect) {
            LogUtils.debug("连接关闭 " + this);
        }
        IOUtils.safeClose(getInputStream(), getOutputStream());
        this.isConnect = false;
    }

    public abstract void connect() throws IOException;

    public abstract InputStream getInputStream();

    public abstract OutputStream getOutputStream();

}
