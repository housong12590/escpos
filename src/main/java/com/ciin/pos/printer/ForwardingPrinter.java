package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.ByteUtils;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 网络转发打印机
 */
public class ForwardingPrinter extends AbstractPrinter {

    private final SocketConnection connection;
    private static final String PING = "ping";
    private static byte[] hb;
    private String host;
    private int port;

    static {
        byte[] data = PING.getBytes(Charset.forName("UTF-8"));
        byte[] head = ByteUtils.intToByteArray(data.length);
        ByteBuffer buffer = new ByteBuffer();
        buffer.write(head);
        buffer.write(data);
        hb = buffer.toByteArray();
    }

    public ForwardingPrinter(Device device, String host) {
        super(device);
        this.host = host;
        this.port = Constants.PRINTER_PORT;
        connection = new SocketConnection(host, Constants.PRINTER_PORT, Constants.SOCKET_TIMEOUT, false);
    }

    private boolean checkConnect() {
        if (!connection.isConnect()) {
            return false;
        }
        byte[] buff = new byte[8];
        try {
            int readLength = this.connection.writeAndRead(hb, buff);
            return readLength == buff.length;
        } catch (IOException ignored) {
        }
        return false;
    }

    @Override
    protected boolean print0(PrintTask printTask) throws TemplateParseException {
        if (printTask.isTimeout()) {
            throw new TimeoutException();
        }
        try {
            // 获取打印数据
            byte[] data = printTask.printData();
            ByteBuffer buffer = new ByteBuffer();
            byte[] dataLength = ByteUtils.intToByteArray(data.length);
            // 写入4字节的长度信息
            buffer.write(dataLength);
            // 写入data数据
            buffer.write(data);
            connection.writeAndFlush(buffer.toByteArray());
            LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "转发打印机";
        }
        return printerName;
    }

    @Override
    protected void printEnd() {
        this.connection.close();
    }

    @Override
    public boolean available() {
        if (!connection.isConnect()) {
            connection.tryConnect();
        }
        return checkConnect();
    }

    @Override
    public void close() {
        super.close();
        if (this.connection.isConnect()) {
            this.connection.close();
            LogUtils.debug(String.format("连接断开 %s:%s", host, this.port));
        }
    }
}
