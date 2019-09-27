package com.ciin.pos.printer;

import com.ciin.pos.Constants;
import com.ciin.pos.callback.OnPrinterListener;
import com.ciin.pos.connect.SocketConnection;
import com.ciin.pos.connect.ReConnectCallback;
import com.ciin.pos.device.Device;
import com.ciin.pos.exception.TemplateParseException;
import com.ciin.pos.exception.TimeoutException;
import com.ciin.pos.util.ByteBuffer;
import com.ciin.pos.util.ByteUtils;
import com.ciin.pos.util.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 网络转发打印机
 */
public class ForwardingPrinter extends AbstractPrinter {

    private final SocketConnection connection;
    private static final String PING = "ping";
    private static byte[] hb;

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
        connection = new SocketConnection(host, Constants.PRINTER_PORT, Constants.SOCKET_TIMEOUT, false);
    }

    private boolean checkConnect() throws IOException {
        if (!connection.isConnect()) {
            return false;
        }
        byte[] buff = new byte[8];
        int readLength = this.connection.writeAndRead(hb, buff);
        return readLength == buff.length;
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
            // 获取打印数据
            byte[] data = printTask.printData();
            ByteBuffer buffer = new ByteBuffer();
            byte[] dataLength = ByteUtils.intToByteArray(data.length);
            // 写入4字节的长度信息
            buffer.write(dataLength);
            // 写入data数据
            buffer.write(data);
            LogUtils.debug(String.format("%s 发送打印数据 %s 字节 ", printTask.getTaskId(), data.length));
            connection.writeAndFlush(buffer.toByteArray());
            return true;
        } catch (IOException e) {
            reconnect(printTask);
        }
        return false;
    }

    // 重新连接
    private void reconnect(PrintTask printTask) {
        OnPrinterListener printerErrorCallback = getPrinterListener();
        this.connection.reConnect(Constants.RECONNECT_INTERVAL, new ReConnectCallback() {
            @Override
            public boolean condition() {
                return !(printTask.isTimeout() || isClose());
            }

            @Override
            public void onFailure(Throwable ex) {
                if (printerErrorCallback != null) {
                    printerErrorCallback.onConnectError(ForwardingPrinter.this, ex);
                }
            }
        });
    }

    @Override
    protected void printEnd() {
        this.connection.close();
    }

    @Override
    public void close() {
        super.close();
        this.connection.close();
    }
}
