package com.hstmpl.escpos.printer;

import com.hstmpl.escpos.connect.Connection;
import com.hstmpl.escpos.device.Device;
import com.hstmpl.escpos.exception.PrintTimeoutException;
import com.hstmpl.escpos.orderset.OrderSet;
import com.hstmpl.escpos.util.LogUtils;

import java.io.IOException;

/**
 * @author hous
 */
public abstract class AbstractIOStreamPrinter extends AbstractPrinter {

    protected Connection connection;

    public AbstractIOStreamPrinter(Device device, Connection connection) {
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
    protected boolean print0(PrintTask printTask) throws Exception {
        if (printTask.isTimeout()) {
            throw new PrintTimeoutException();
        }
        byte[] data = printTask.printData();
        try {
            connection.writeAndFlush(data);
            LogUtils.debug(String.format("%s send print data %s byte", printTask.getTaskId(), data.length));
            return true;
        } catch (IOException ignored) {

        }
        return false;
    }

    @Override
    protected void printEnd0() {
        this.connection.close();
    }
}
