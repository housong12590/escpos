package com.ciin.pos.printer;

import com.ciin.pos.connect.Connection;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.LogUtils;

import java.io.IOException;

public abstract class IOStreamPrinter extends AbstractPrinter {

    protected Connection connection;

    public IOStreamPrinter(Device device, Connection connection) {
        super(device);
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    protected boolean checkConnect() {
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
        printEnd();
        super.close();
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
            LogUtils.debug(String.format("连接断开 %s", this.connection));
        }
    }
}
