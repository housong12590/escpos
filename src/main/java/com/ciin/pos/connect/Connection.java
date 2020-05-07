package com.ciin.pos.connect;

import java.io.IOException;

public interface Connection {

    /**
     * 开始连接
     */
    void doConnect() throws IOException;

    /**
     * 尝试连接
     */
    boolean tryConnect();

    /**
     * 尝试连接
     */
    boolean tryConnect(OnConnectCallback onConnectCallback);

    /**
     * 是否连接成功
     */
    boolean isConnect();

    /**
     * 写入数据
     */
    void write(byte[] data) throws IOException;

    /**
     * 写入并刷到目标缓冲区
     */
    void writeAndFlush(byte[] data) throws IOException;

    /**
     * 写入并读取目标返回的数据
     *
     * @param wb 写入的数据
     * @param rb 读取的数据缓存区
     * @return 读取字节长度
     */
    int writeAndRead(byte[] wb, byte[] rb) throws IOException;

    /**
     * 冲刷到目标缓冲区
     */
    void flush() throws IOException;

    /**
     * 读取数据
     *
     * @param buff 读取缓冲区
     * @return 返回读取字节长度
     */
    int read(byte[] buff) throws IOException;

    /**
     * 关闭连接通道
     */
    void close();
}
