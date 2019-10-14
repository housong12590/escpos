package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.Connection;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.device.DeviceFactory;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.StringUtils;

import java.io.IOException;

/**
 * 网络打印机
 */
public class NetworkPrinter extends AbstractPrinter {

    private String host;
    private int port;
    private int timeout;
    private Connection connection;

    public NetworkPrinter(String host) {
        this(DeviceFactory.getDefault(), host);
    }

    public NetworkPrinter(Device device, String host) {
        this(device, host, Constants.PRINTER_PORT, Constants.SOCKET_TIMEOUT);
    }

    public NetworkPrinter(Device device, String host, int port, int timeout) {
        super(device);
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        connection = new SocketConnection(host, port, timeout, true);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }

    @Override
    public String getPrinterName() {
        String printerName = super.getPrinterName();
        if (StringUtils.isEmpty(printerName)) {
            return "网口打印机:" + host;
        }
        return printerName;
    }

    private boolean checkConnect() {
        if (!connection.isConnect()) {
            return false;
        }
        OrderSet orderSet = this.getDevice().getOrderSet();
        byte[] buff = new byte[48];
        try {
            int readLength = this.connection.writeAndRead(orderSet.status(), buff);
            return readLength != -1;
        } catch (IOException e) {
            return false;
        }
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
        this.connection.close();
    }

    @Override
    protected boolean print0(PrintTask printTask) throws TemplateParseException {
        if (printTask.isTimeout()) {
            throw new TimeoutException();
        }
        byte[] data = printTask.printData();
        try {
            connection.writeAndFlush(data);
            LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
            return true;
        } catch (IOException ignored) {

        }
        return false;
    }

    @Override
    protected void printEnd() {
        this.connection.close();
        if (connection.isConnect()) {
            LogUtils.debug(String.format("连接断开 %s:%s", host, port));
        }
    }
}
