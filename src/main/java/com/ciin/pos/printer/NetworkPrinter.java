package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.callback.OnPrinterErrorListener;
import com.ciin.pos.connect.Connection;
import com.ciin.pos.connect.ReConnectCallback;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.device.DeviceFactory;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.LogUtils;

import java.io.IOException;

/**
 * 网络打印机
 */
public class NetworkPrinter extends AbstractPrinter {

    private String host;
    private int port;
    private int timeout;
    private Connection mConnection;

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
        mConnection = new SocketConnection(host, port, timeout, true);
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

    private boolean checkConnect() throws IOException {
        if (!mConnection.isConnect()) {
            return false;
        }
        OrderSet orderSet = this.getDevice().getOrderSet();
        byte[] buff = new byte[48];
        int readLength = this.mConnection.writeAndRead(orderSet.status(), buff);
        return readLength != -1;
    }

    @Override
    public void close() {
        super.close();
        this.mConnection.close();
    }

    @Override
    protected boolean print0(PrintTask printTask) throws TemplateParseException {
        try {
            if (!checkConnect()) {
                reconnect(printTask);
            }
            if (printTask.isTimeout()) {
                throw new TimeoutException();
            }
            byte[] data = printTask.printData();
            LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
            mConnection.writeAndFlush(data);
            return true;
        } catch (IOException e) {
            reconnect(printTask);
        }
        return false;
    }

    // 重新连接
    private void reconnect(PrintTask printTask) {
        OnPrinterErrorListener printerErrorCallback = getPrinterErrorListener();
        this.mConnection.reConnect(Constants.RECONNECT_INTERVAL, new ReConnectCallback() {
            @Override
            public boolean condition() {
                return !(printTask.isTimeout() || isClose());
            }

            @Override
            public void onFailure(Throwable ex) {
                if (printerErrorCallback != null) {
                    printerErrorCallback.onConnectError(NetworkPrinter.this, ex);
                }
            }
        });
    }

    @Override
    protected void printEnd() {
        this.mConnection.close();
        LogUtils.debug("连接断开");
    }
}
