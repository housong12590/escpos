package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.connect.Connection;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.device.Device;
import com.ciin.pos.device.DeviceFactory;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.orderset.OrderSet;
import com.ciin.pos.util.LogUtils;
import com.ciin.pos.util.Utils;

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
        OrderSet orderSet = this.mDevice.getOrderSet();
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
                retryConnection(printTask);
            }
            byte[] data = printTask.printData();
            LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
            mConnection.writeAndFlush(data);
            return true;
        } catch (IOException e) {
            retryConnection(printTask);
        }
        return false;
    }

    // 重新连接
    private void retryConnection(PrintTask printTask) {
        while (!this.mConnection.isConnect() && !close) {
            if (printTask != null && printTask.isTimeOut()) {
                return;
            }
            try {
                try {
                    // 建立打印机连接
                    this.mConnection.doConnect();
                } catch (IOException e) {
                    if (mPrinterErrorCallback != null) {
                        LogUtils.error("连接异常, 正在尝试重连  " + e.getMessage());
                        mPrinterErrorCallback.onConnectError(this, mConnection);
                    }
                    throw e;
                }
                try {
                    // 检测打印机是否可用
                    checkConnect();
                } catch (IOException e) {
                    if (mPrinterErrorCallback != null) {
                        LogUtils.error("打印机异常, 请重启打印机  " + e.getMessage());
                        mPrinterErrorCallback.onPrinterError(this);
                    }
                    throw e;
                }
                LogUtils.debug(" 连接成功");
            } catch (Exception ex) {
                Utils.sleep(5000);
            }
        }
    }

    @Override
    protected void printEnd() {
        this.mConnection.close();
        LogUtils.debug("连接断开");
    }
}
